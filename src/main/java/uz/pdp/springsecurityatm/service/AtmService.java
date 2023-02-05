package uz.pdp.springsecurityatm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityatm.entity.*;
import uz.pdp.springsecurityatm.entity.enums.CardName;
import uz.pdp.springsecurityatm.entity.enums.USD;
import uz.pdp.springsecurityatm.entity.enums.UZS;
import uz.pdp.springsecurityatm.payload.AtmDTO;
import uz.pdp.springsecurityatm.payload.TakeOutDto;
import uz.pdp.springsecurityatm.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@Service
public class AtmService {

    private final ATMRepository repository;
    private final BankRepository bankRepository;
    private final CardTypeRepository cardTypeRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final SummaRepository summaRepository;
    private final DollarRepository dollarRepository;

    @Autowired
    public AtmService(ATMRepository repository,
                      BankRepository bankRepository,
                      CardTypeRepository cardTypeRepository,
                      UserRepository userRepository, AddressRepository addressRepository, SummaRepository summaRepository, DollarRepository dollarRepository) {
        this.repository = repository;
        this.bankRepository = bankRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.summaRepository = summaRepository;
        this.dollarRepository = dollarRepository;
    }

    public ResponseEntity<?> getAllATMs() {
        return ok(repository.findAll());
    }

    public ResponseEntity<?> getOneAtm(Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> getSumma(String type) {
        if (type.equalsIgnoreCase("uzs")) return ok(UZS.values());
        else if (type.equalsIgnoreCase("usd")) return ok(USD.values());
        else return badRequest().body("Wrong type");
    }

    public ResponseEntity<?> saveATM(AtmDTO dto) {
        Optional<Bank> optionalBank = bankRepository.findById(dto.getBankId());
        if (optionalBank.isEmpty()) return status(NOT_FOUND).body("Bank not found");
        Optional<User> optionalUser = userRepository.findById(dto.getUserId());
        if (optionalUser.isEmpty()) return status(NOT_FOUND).body("User not found");
        Address address = addressRepository.save(new Address(null, dto.getCity(), dto.getStreet(), dto.getDistrict()));
        try {
            List<Summa> summas = summaRepository.saveAll(dto.getSums());
            List<Dollar> dollars = dollarRepository.saveAll(dto.getDollars());
            ATM atm = new ATM(
                    checkCardType(dto.getCardTypes()),
                    optionalBank.get(),
                    address,
                    new HashSet<>(summas),
                    new HashSet<>(dollars),
                    optionalUser.get(),
                    getBalance(dto.getSums()),
                    dto.getCommission(),
                    dto.getTakeOut()
            );
            repository.save(atm);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            addressRepository.delete(address);
            return badRequest().body("ATM not saved");
        }
        return ok("atm successfully saved");
    }

    public ResponseEntity<?> deleteAtm(Long id) {
        return repository.findById(id)
                .map(atm -> {
                    try {
                        addressRepository.delete(atm.getAddress());
                        repository.delete(atm);
                        return status(NO_CONTENT).body("atm successfully deleted");
                    } catch (Exception e) {
                        return badRequest().body("ATM not deleted");
                    }
                }).orElseGet(() -> status(NOT_FOUND).body("ATM not found"));
    }

    public ResponseEntity<?> editAtm(Long id, AtmDTO atmDTO) {
        Optional<ATM> optionalATM = repository.findById(id);
        if (optionalATM.isEmpty()) return status(NOT_FOUND).body("ATM not found");
        Optional<Bank> optionalBank = bankRepository.findById(atmDTO.getBankId());
        if (optionalBank.isEmpty()) return status(NOT_FOUND).body("Bank not found");
        ATM atm = optionalATM.get();
        Address address = atm.getAddress();
        address.setCity(atmDTO.getCity());
        address.setDistrict(atmDTO.getDistrict());
        address.setStreet(atmDTO.getStreet());
        try {
            addressRepository.save(address);
        } catch (Exception e) {
            return status(BAD_REQUEST).body("Address not saved");
        }
        atm.setDollars(atmDTO.getDollars());
        Optional<User> optionalUser = userRepository.findById(atmDTO.getUserId());
        optionalUser.ifPresent(atm::setUser);
        atm.setCardType(checkCardType(atmDTO.getCardTypes()));
        atm.setCommission(atm.getCommission());
        atm.setMaxWithdraw(atmDTO.getTakeOut());
        atm.setSummas(atmDTO.getSums());
        atm.setBalance(getBalance(atmDTO.getSums()));
        try {
            repository.save(atm);
            return status(CREATED).body("ATM successfully edited");
        } catch (Exception e) {
            return status(BAD_REQUEST).body("ATM failed to saved");
        }
    }

    // Actions
    public Set<CardType> checkCardType(Set<Integer> cardTypes) {
        return new HashSet<>(cardTypeRepository.findAllById(cardTypes));
    }

    public Double getBalance(Set<Summa> summas) {
        double balance = 0D;
        for (Summa summa : summas) {
            balance += Double.parseDouble(summa.getUzs().name().substring(1)) * summa.getCount();
        }
        return balance;
    }

    public ResponseEntity<?> takeOut(Long id, TakeOutDto dto) {
        if (dto.getMoneyType().equalsIgnoreCase("uzs") || dto.getMoneyType().equalsIgnoreCase("usd")) {
            Optional<ATM> optionalATM = repository.findById(id);
            if (optionalATM.isEmpty()) return status(NOT_FOUND).body("ATM not found");
            ATM atm = optionalATM.get();
            Card card = (Card) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (dto.getMoneyType().equals("uzs") && (card.getType().getType().equals(CardName.HUMO) || card.getType().getType().equals(CardName.VISA))) {
                if (card.getBalance() >= dto.getMoney() + (dto.getMoney() * atm.getCommission() / 100)) {
                    return withdrawMoneyForSum(dto.getMoney(), atm, card);
                }
                return badRequest().body("Not enough money");
            } else if (dto.getMoneyType().equals("usd") && (card.getType().getType().equals(CardName.VISA))) {
                //
            }
            return null;
        }
        return status(BAD_REQUEST).body("Wrong money type");
    }

    public ResponseEntity<?> withdrawMoneyForSum(Double money, ATM atm, Card card) {
        int _hundred = 0, _fifty = 0, _five = 0, _ten = 0, _one = 0;
        Set<Summa> summas = atm.getSummas();
        for (Summa summa : summas) {
            if (money >= 100_000 && summa.getUzs().equals(UZS._100000) && ((int) (money / 100_000)) >= summa.getCount()) {
                _hundred = (int) (money / 100_000);
                money = money - _hundred * 100_000;
            }
            if (money >= 50_000 && summa.getUzs().equals(UZS._50000) && ((int) (money / 50_000)) >= summa.getCount()) {
                _fifty = (int) (money / 50_000);
                money = money - _fifty * 50_000;
            }
            if (money >= 10_000 && summa.getUzs().equals(UZS._10000) && ((int) (money / 10_000)) >= summa.getCount()) {
                _ten = (int) (money / 10_000);
                money = money - _ten * 10_000;
            }
            if (money >= 5_000 && summa.getUzs().equals(UZS._5000) && ((int) (money / 5_000)) >= summa.getCount()) {
                _five = (int) (money / 5_000);
                money = money - _five * 5_000;
            }
            if (money >= 1_000 && summa.getUzs().equals(UZS._1000) && ((int) (money / 1_000)) >= summa.getCount()) {
                _five = (int) (money / 1_000);
                money = money - _five * 1_000;
            }
        }
        System.out.println(money);
        return ok(money);
    }


//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter amount: ");
//        double money = scanner.nextDouble();
//        int _hundred = 0, _fifty = 0, _five = 0, _ten = 0, _one = 0;
//        if (money >= 100_000) {
//            _hundred = (int) money / 100_000;
//            money = money - _hundred * 100_000;
//        }
//        if (money >= 50_000) {
//            _fifty = (int) money / 50_000;
//            money = money - _fifty * 50_000;
//        }
//        if (money >= 10_000) {
//            _ten = (int) money / 10_000;
//            money = money - _ten * 10_000;
//        }
//        if (money >= 5_000) {
//            _five = (int) money / 5_000;
//            money = money - _five * 5_000;
//        }
//        if (money >= 1_000) {
//            _five = (int) money / 1_000;
//            money = money - _five * 1_000;
//        }
//        System.out.println("yuz ming so'm donasi: " + _hundred);
//        System.out.println("ellik ming so'm donasi: " + _fifty);
//        System.out.println("o'n ming so'm donasi: " + _ten);
//        System.out.println("besh ming so'm donasi: " + _five);
//        System.out.println("ming so'm donasi: " + _one);
//    }
}
