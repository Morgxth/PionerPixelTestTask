package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.EmailData;
import com.example.demo.entity.PhoneData;
import com.example.demo.entity.User;
import com.example.demo.repository.EmailDataRepository;
import com.example.demo.repository.PhoneDataRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailRepo;
    private final PhoneDataRepository phoneRepo;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmails_Email(email);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhones_Phone(phone);
    }

    @Override
    @CacheEvict(value = {"users", "usersByEmail", "usersByPhone"}, key = "#userId")
    public void addEmail(Long userId, String email) {
        if (emailRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email уже занят другим пользователем");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        emailData.setUser(user);

        user.getEmails().add(emailData);
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = {"users", "usersByEmail", "usersByPhone"}, key = "#userId")
    public void removeEmail(Long userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        List<EmailData> emails = user.getEmails();
        if (emails.size() <= 1) {
            throw new IllegalStateException("У пользователя должен быть хотя бы один email");
        }

        EmailData toRemove = emails.stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Email не найден у пользователя"));

        emails.remove(toRemove);
        emailRepo.delete(toRemove);
    }

    @Override
    public void addPhone(Long userId, String phone) {
        if (phoneRepo.existsByPhone(phone)) {
            throw new IllegalArgumentException("Телефон уже используется другим пользователем");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        phoneData.setUser(user);

        user.getPhones().add(phoneData);
        userRepository.save(user);
    }

    @Override
    public void removePhone(Long userId, String phone) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        List<PhoneData> phones = user.getPhones();
        if (phones.size() <= 1) {
            throw new IllegalStateException("У пользователя должен быть хотя бы один телефон");
        }

        PhoneData toRemove = phones.stream()
                .filter(e -> e.getPhone().matches(phone))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Телефон не найден у пользователя"));
        phones.remove(toRemove);
        phoneRepo.delete(toRemove);
    }

    @Override
    public List<UserDto> searchUsers(String name, String email, String phone, LocalDate dob, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (name != null) {
            return userRepository.findByNameStartingWith(name, pageable)
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } else if (email != null) {
            return userRepository.findByEmails_Email(email)
                    .map(user -> List.of(mapToDto(user)))
                    .orElse(List.of());
        } else if (phone != null) {
            return userRepository.findByPhones_Phone(phone)
                    .map(user -> List.of(mapToDto(user)))
                    .orElse(List.of());
        } else if (dob != null) {
            return userRepository.findByDateOfBirthAfter(dob, pageable)
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        return userRepository.findAll(pageable)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());

        if (user.getAccount() != null) {
            AccountDto accountDto = new AccountDto();
            accountDto.setId(user.getAccount().getId());
            accountDto.setBalance(user.getAccount().getBalance());
            dto.setAccount(accountDto);
        }
        dto.setEmails(user.getEmails().stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList()));

        dto.setPhones(user.getPhones().stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList()));

        return dto;
    }
}
