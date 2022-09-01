package com.qkrtkdwns3410.webflux.domain;

/**
 * packageName    : com.qkrtkdwns3410.webflux
 * fileName       : Customer
 * author         : qkrtkdwns3410
 * date           : 2022-09-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-01        qkrtkdwns3410       최초 생성
 */

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
public class Customer {
      
      @Id
      private Long id;
      private final String firstName;
      private final String lastName;
      
}
