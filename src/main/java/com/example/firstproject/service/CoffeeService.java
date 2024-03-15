package com.example.firstproject.service;

import com.example.firstproject.dto.CoffeeDto;
import com.example.firstproject.entity.Coffee;
import com.example.firstproject.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Slf4j
@Service
public class CoffeeService {

    @Autowired
    CoffeeRepository coffeeRepository;

    public List<Coffee> index() {
        return coffeeRepository.findAll();
    }

    public Coffee show(Long id) {
        return coffeeRepository.findById(id).orElse(null);
    }

    public Coffee create(CoffeeDto dto) {
        Coffee coffee = dto.toEntity();
        if (coffee.getId() != null) {
            return null;
        }
        return coffeeRepository.save(coffee);
    }

    public Coffee update(Long id, CoffeeDto dto) {
        // 1. DTO를 엔티티로 변환
        Coffee coffee = dto.toEntity();
        log.info("id: {}, coffee: {}", id, coffee.toString());
        // 2. 타겟 조회
        Coffee target = coffeeRepository.findById(id).orElse(null);
        // 3. 잘못된 요청 처리
        if (target == null || id != coffee.getId()) {
            // 400, 잘못된 요청 응답
            log.info("잘못된 요청!! id: {}, coffee:{}", id, coffee, coffee.toString());
            return null;
        }
        // 4. 업데이트 및 정상 응답(200)
        Coffee updated = coffeeRepository.save(coffee);
        return updated;
    }

    public Coffee delete(Long id) {
        // 1. 대상 찾기
        Coffee target = coffeeRepository.findById(id).orElse(null);
        // 2. 잘못된 요청 처리
        if (target == null) {
            return null;
        }
        // 3. 대상 삭제
        coffeeRepository.delete(target);
        return target;
    }
}