package com.qkrtkdwns3410.webflux.controller;

import com.qkrtkdwns3410.webflux.domain.Customer;
import com.qkrtkdwns3410.webflux.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

/**
 * packageName    : com.qkrtkdwns3410.webflux.controller
 * fileName       : CustomerController
 * author         : qkrtkdwns3410
 * date           : 2022-09-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-09-01        qkrtkdwns3410       최초 생성
 */
@RestController
public class CustomerController {
      
      private final CustomerRepository customerRepository;
      private final Sinks.Many<Customer> sink;
      
      // A 요청 -> Flux -> Stream
      // B 요청 -> Flux -> Stream
      // -> Flux.merge -> Sink
      
      public CustomerController(CustomerRepository customerRepository) {
            this.customerRepository = customerRepository;
            this.sink = Sinks.many()
                             .multicast()
                             .onBackpressureBuffer();
      }
      
      /*
      2022-09-01 21:20:44.498  INFO 13064 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.1                 : onSubscribe(FluxUsingWhen.UsingWhenSubscriber)
      2022-09-01 21:20:44.501  INFO 13064 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.1                 : request(unbounded)
      // unbound -> 모두를 들고오겠다고 scope 설정
      2022-09-01 21:20:44.505  INFO 13064 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.1                 : onNext(Customer(id=1, firstName=Jack, lastName=Bauer))
      2022-09-01 21:20:44.505  INFO 13064 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.1                 : onNext(Customer(id=2, firstName=Chloe, lastName=O'Brian))
      2022-09-01 21:20:44.505  INFO 13064 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.1                 : onNext(Customer(id=3, firstName=Kim, lastName=Bauer))
      2022-09-01 21:20:44.505  INFO 13064 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.1                 : onNext(Customer(id=4, firstName=David, lastName=Palmer))
      2022-09-01 21:20:44.505  INFO 13064 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.1                 : onNext(Customer(id=5, firstName=Michelle, lastName=Dessler))
      2022-09-01 21:20:44.506  INFO 13064 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.1                 : onComplete()
      // Complete 되는 순간 모두 완료
      */
//      @GetMapping("/customer")
//      public Flux<Customer> findAll() {
//            return customerRepository.findAll()
//                                     .log();
//      }
      
      @GetMapping("/flux")
      public Flux<Integer> flux() {
            return Flux.just(1, 2, 3, 4, 5)
                       .delayElements(Duration.ofSeconds(1))
                       .log();
      }
      
      /* 2022-09-01 21:27:28.256  INFO 21220 --- [ctor-http-nio-3] reactor.Flux.ConcatMap.3                 : onSubscribe(FluxConcatMap.ConcatMapImmediate)
       2022-09-01 21:27:28.256  INFO 21220 --- [ctor-http-nio-3] reactor.Flux.ConcatMap.3                 : request(unbounded)
      1초
       2022-09-01 21:27:29.272  INFO 21220 --- [     parallel-7] reactor.Flux.ConcatMap.3                 : onNext(1)
       1초
       2022-09-01 21:27:30.279  INFO 21220 --- [     parallel-8] reactor.Flux.ConcatMap.3                 : onNext(2)
       1초
       2022-09-01 21:27:31.288  INFO 21220 --- [     parallel-9] reactor.Flux.ConcatMap.3                 : onNext(3)
       1초
       2022-09-01 21:27:32.289  INFO 21220 --- [    parallel-10] reactor.Flux.ConcatMap.3                 : onNext(4)
       1초
       2022-09-01 21:27:33.298  INFO 21220 --- [    parallel-11] reactor.Flux.ConcatMap.3                 : onNext(5)
       2022-09-01 21:27:33.298  INFO 21220 --- [    parallel-11] reactor.Flux.ConcatMap.3                 : onComplete()*/
      @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
      public Flux<Integer> fluxstream() {
            return Flux.just(1, 2, 3, 4, 5)
                       .delayElements(Duration.ofSeconds(1))
                       .log();
      }
      
      @GetMapping(value = "/customer", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
      public Flux<Customer> findAll() {
            return customerRepository.findAll()
                                     .delayElements(Duration.ofSeconds(1))
                                     .log();
      }
      
      /* 2022-09-01 21:32:29.968  INFO 10932 --- [ctor-http-nio-2] reactor.Mono.UsingWhen.3                 : onSubscribe(MonoUsingWhen.MonoUsingWhenSubscriber)
       2022-09-01 21:32:29.968  INFO 10932 --- [ctor-http-nio-2] reactor.Mono.UsingWhen.3                 : request(unbounded)
       2022-09-01 21:32:29.970  INFO 10932 --- [ctor-http-nio-2] reactor.Mono.UsingWhen.3                 : onNext(Customer(id=2, firstName=Chloe, lastName=O'Brian))
                                                                                                                                                           2022-09-01 21:32:29.971  INFO 10932 --- [ctor-http-nio-2] reactor.Mono.UsingWhen.3                 : onComplete()*/
      // Mono 반환 타입의 경우 -> 단건으로 데이터를 받는 경우 해당 나머지 FLUX
      @GetMapping("/customer/{id}")
      public Mono<Customer> findById(@PathVariable Long id) {
            return customerRepository.findById(id)
                                     .log();
      }
      
      @GetMapping(value = "/customer/sse") // 생략 - produces = MediaType.TEXT_EVENT_STREAM_VALUE
      // 자바 스크립트에서 이벤트로 해당 값을 받을 수 있음.
      public Flux<ServerSentEvent<Customer>> findAllSSE() {
            return sink.asFlux()
                       .map(c -> ServerSentEvent.builder(c)
                                                .build())
                       .doOnCancel(() -> {
                             sink.asFlux()
                                 .blockLast();
                       });
      }
      
      @PostMapping("/customer")
      public Mono<Customer> save() {
            return customerRepository.save(new Customer("gildong", "hong"))
                                     .doOnNext(sink::tryEmitNext);
      }
}
