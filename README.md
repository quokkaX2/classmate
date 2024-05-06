![1](https://github.com/quokkaX2/classmate/assets/129869700/5d7c7e28-a4ea-4a22-b63a-44a07b82ca7b)

# 📝 1. 프로젝트 및 팀원 소개

<a href="https://prism-horse-a26.notion.site/Classmate-b9b08e51ffd441cea867a7aae5d1c930" target="_blank">팀 브로셔</a>

| 이름 | 역할 |
| --- | --- |
| <a href="https://github.com/kimD0ngjun" target="_blank">김동준(TL)</a> | - 코드 기능 개발 및 테스트코드 기능 단위 작성<br />- Prometheus + Grafana 기반 초기 모니터링 툴 구축<br />- Jmeter를 활용한 부하 테스트, 성능 테스트<br />- 데이터 동시성 제어<br />- 쿼리문 발신 최적화<br />- Elastic Search + Log Stash + Kibana 구축을 통한 검색 성능 개선<br />- Front-end 스켈레톤 프레임 작성 및 개선  |
| <a href="https://github.com/yoonion" target="_blank">김상윤</a> | - 코드 기능 개발 및 테스트코드 기능 단위 작성<br />- Docker + Prometheus + Grafana 서버 모니터링 툴 구축<br />- Jmeter를 활용한 부하 테스트, 성능 테스트<br />- 데이터 동시성 제어<br />- Redis 기반 캐싱 처리<br />- 쿼리문 발신 최적화<br />- 오토 스케일링 분산 환경 스케줄 락 적용|
| <a href="https://github.com/20hyukim" target="_blank">김현영</a> |  |
| <a href="https://github.com/qkrckstjq" target="_blank">박찬섭</a> |  |
| <a href="https://github.com/klarra247" target="_blank">이시현</a> | - 코드 기능 개발 및 테스트코드 기능 단위 작성<br />- Prometheus + Grafana 모니터링 툴 구축<br />- 강의 데이터 수집 및 전처리 후 DB 적재<br />- Jmeter를 활용한 부하 테스트, 성능 테스트<br />- 데이터 동시성 제어<br />- Elastic Search + Log Stash + Kibana 구축을 통한 검색 성능 개선 <br />- Front-end 작업|

---

## 👩🏻‍💻 **더 이상 못할 수강신청은 없다! 동시 접속자가 많아져도 서버 걱정 없이 편리하게 신청하자!**

<aside>

고가용성과 안정성을 중심으로 한 기술 도입으로 뛰어난 성능 보장:

1️⃣  **수강신청 대기열** 구현: 신속한 처리와 공정한 접근성을 보장합니다.

2️⃣  **동시성 제어**: 데이터 일관성과 정확성을 유지합니다.

3️⃣  **Auto Scaling과 로드 밸런서**: 트래픽 부하를 효과적으로 관리하여, 서비스가 항상 원활하게 작동하도록 합니다.

4️⃣  **DB Replication**: 데이터베이스 부하를 분산시켜 높은 트래픽에도 안정적인 성능을 제공합니다.

5️⃣  **Elastic Search - Log Stash - Kibana 체계**: 빠르고 정확한 검색 기능을 통해 사용자 경험을 향상시킵니다.

대규모 트래픽 부하 테스트를 거친 결과, 한 번에 10만 명의 수강 신청을 약 **1500 TPS**의 처리량으로 문제 없이 처리할 수 있으며, 동시에 20000명의 검색 요청도 평균 6-700ms의 응답 속도로 처리가 가능한 고가용성, 안정성, 신뢰성을 확보한 프로젝트 입니다.

**CLASSMATE**에서 테크놀로지가 만들어내는 안정성을 **직접** 경험하세요.

</aside>

---

# 🖥️ 2. 주요 기능 및 성능 개선

## ❗️대기열 구현

- Redis의 sorted set 자료구조로 수강 신청 순서 보장
- 웹 소켓을 활용하여 사용자에게 실시간 대기열 정보 제공

<img width="928" alt="2" src="https://github.com/quokkaX2/classmate/assets/129869700/642438cf-3fa2-4e92-9b1a-c98cd552f416">

## ❗️Redis를 활용한 수강신청 TPS, Latency 개선

- I/O가 빠른 In Memory 데이터 베이스인 Redis로 수강 신청 잔여석을 캐시 하여 응답 속도, TPS 개선
- 수강 신청 잔여석이 ‘0’일 경우, 비즈니스 로직 이전 Redis에서 잔여석을 확인하여 바로 마감 응답

---

![3](https://github.com/quokkaX2/classmate/assets/129869700/3efc37d1-2fca-42d1-8d82-4b48d5d941c9)

---

![4](https://github.com/quokkaX2/classmate/assets/129869700/728469b2-b074-4059-a930-f81e8c40d01b)

---

### ✔️ 테스트 결과

### 50000명 10초 동안 요청 - Redis 적용 전

<img width="1187" alt="5" src="https://github.com/quokkaX2/classmate/assets/129869700/7ee9986f-eefd-4c9a-849d-878fa3ef5bcb">

평균 응답시간 : **810ms**

TPS : **234**

### 50000명 10초 동안 요청 - Redis 적용 후

<img width="1175" alt="6" src="https://github.com/quokkaX2/classmate/assets/129869700/919f1730-0e6a-4dca-a613-6a7675e012be">

평균 응답시간 : **297ms**

TPS : **598**

- 5만명 요청 기준
    - 평균 응답시간 :  **`810ms` → `297ms` 약 63% 개선**
    - TPS : **`234 TPS` → `598 TPS` 약 155% 개선**
    

## ❗️Elastic Search를 활용한 검색 성능 개선

- **서버 성능 향상**: 대용량 데이터 처리를 위해 서버 성능을 AWS t2.small에서 t3.medium으로 업그레이드함으로서 처리 능력과 반응 속도 개선
- **페이징 전략 개선**: 대용량 데이터 검색의 효율성을 높이기 위해 offset 기반 페이징에서 성능이 우수한 cursor 기반 페이징으로 전환, 그러나 초기 테스트에서는 평균 TPS 2.49/sec, 평균 응답 시간 18067.46 ms로 기대에 매우 미치지 못하는 결과를 보임
- **네이티브 쿼리 적용**: MySQL에 최적화된 네이티브 쿼리를 사용하여 성능 테스트를 진행하였을 때, 500명이 10초 동안 검색할 경우 평균 TPS 112.38/sec, 평균 응답 시간 876.04 ms로 크게 개선됨
- **인덱싱 최적화**: 제목 필드에 인덱싱을 적용하여, 대용량 데이터 검색 시 뒷부분 데이터 검색 지연 문제를 해결. 이로 인해 20,000명이 동시에 검색할 경우 평균 TPS 253.8/sec, 평균 응답 시간 762 ms로 성능이 개선됨
- **Elasticsearch 도입**: 초기 도입 시 Elasticsearch의 성능이 저조했으나 (평균 TPS 54.2/sec, 평균 응답 시간 3580 ms), 쿼리 조정과 Nori tokenizer 적용 후 성능이 크게 향상 (평균 TPS 294.3/sec, 평균 응답 시간 650 ms).
- **노드 및 샤드 구성 조정**: CPU 사용량이 너무 높고 검색 테스트 결과가 일정하지 않은 문제를 해결하기 위해 Elasticsearch 노드를 추가 확장하고 샤드와 레플리카의 구성을 조정하여 최적의 성능을 테스트 한 결과, 샤드 2개, 레플리카 0개 설정에서 **평균 TPS 308.4/sec, 평균 응답 시간 637 ms**로 안정적인 성능을 유지하며 최종 구성을 확정함
    
    ![7](https://github.com/quokkaX2/classmate/assets/129869700/3f551706-687d-43c1-9e17-46614ccf048e)

    ![8](https://github.com/quokkaX2/classmate/assets/129869700/e84114bb-b122-4a26-94f2-8198d45dc613)

## ❗️Auto Scaling을 통해 수강 신청 기능 개선

- **EC2 Auto Scaling :** 서버 CPU 점유율을 통해 최대 3개의 EC2가 증설되게 세팅, 이후의 요청은 **ALB**를 통해 **ASG**에 전달됨
- **Redis Shed Lock :** 여러 개의 EC2가 각각의 **스케줄러**에 의해 **Redis**에 접근하여 데이터를 처리할 경우 같은 데이터를 중복 처리하는 이슈 발생, **Shed Lock**을 통해 하나의 EC2가 **스케줄러**를 통해 **Redis**를 사용하고 있다면 다른 EC2의 **스케줄러**가 동시에 **Redis**를 접근하지 못하게 함

**기존 수강 신청 최대 TPS : 500TPS**

![9](https://github.com/quokkaX2/classmate/assets/129869700/b76fb986-f4b5-4e21-9c18-cd2fd5e414c7)

**Auto Scaling 적용 후 최대 TPS : 1515TPS**

![10](https://github.com/quokkaX2/classmate/assets/129869700/7c085491-eac7-4325-ba62-2b0b272602e6)

## ❗️WebSocket 서버 분리

- **WebSocket 전용 서버 독립** : 대기열 기능 구현 당시 UX측면에서 대기열을 실시간으로 보여줄 수 있는 WebSocket 기능을 하나의 서버에 전담 시키지 않고 분리된 독립 서버로 설계
- **역할 분리로 부하 감소** : 기존 서버는 API요청 응답 및 데이터 처리, **WebSocket** 서버는 실시간 소켓 통신만 담당하게 되면서 효율적인 데이터 처리 가능
- **기능 확장에서의 유연함** : **WebSocket** 서버를 따로 분리하지 않았다면 **Auto Scaling** 적용 시 여러 개의 서버가 Redis의 같은 데이터를 처리하게 되면서 불규칙하고 정확하지 않은 데이터를 사용자에게 전달 할 수 있음, **Lock** 적용을 통해 해결할 수 있겠지만 **WebSocket**기능을 가진 여러 개의 서버가 결국 한 번씩만 작동하게 되면서 비효율적으로 작동하게 됨

---

# ❌ 3. 주요 트러블 슈팅

## 수강신청 동시성 문제

### ❗️ 문제 확인

- 한 과목에 대한 수강신청 트래픽 몰릴 경우, 강의 잔여석에 대한 동시성 문제 발생
- 동시성으로 인한 객체 공유로 인해 예상했던 수치의 변동폭과 다른 값이 데이터베이스에서 확인됨

### ✔️ 해결

- 낙관적 락 적용 시, 충돌이 빈번하기 때문에 현재 프로젝트 규모에서 부하가 예상되어 **비관적 락 적용 및 해결**
- 200개의 수강신청 가능 과목 → 1초동안 한 과목에 대해 100개의 수강신청 요청 → **오류없이 100개의 잔여석 남은 것 확인**
  
<img width="640" alt="11" src="https://github.com/quokkaX2/classmate/assets/129869700/7a244bc5-4eab-4413-bbcd-766c11d21628">

<img width="640" alt="12" src="https://github.com/quokkaX2/classmate/assets/129869700/9f880244-205e-46e3-b5a9-b772f680e57c">  

## 분산 서버 환경(오토 스케일링) 대기열 스케줄러 중복 실행 문제

### ❗️ 문제 확인

- 단일 서버에서 동시성 제어가 성공했던 수강신청에 대해 분산 서버(오토 스케일링)을 적용 후 문제 발생
- 강의 잔여석이 부족하지 않은데 일부 요청에서 신청이 마감 되었다는 응답이 발생

![13](https://github.com/quokkaX2/classmate/assets/129869700/34834dab-576c-461b-98fa-58a957af61cf)

### ✔️ 해결

- 분산 서버 환경에서 대기열에 있는 요청을 가져오는 부분이 중복 실행 되는 것 으로 확인하여 동일한 스케줄러가 하나의 인스턴스에서만 작동할 수 있도록 ShedLock을 활용한 스케줄 락 적용

<img width="477" alt="14" src="https://github.com/quokkaX2/classmate/assets/129869700/1518c4b1-82df-41c9-b795-a2aab528b2a5">

- 스케줄러 Lock 정보는 Redis를 이미 사용중이었기 때문에 Redis에 저장하여 Lock 정보를 공유

![15](https://github.com/quokkaX2/classmate/assets/129869700/39a4366f-28ea-473e-83c7-a24db0481df7)

## Elastic Search 검색 대상 데이터 전달 문제

### ❗️ 문제 확인

- 단순 Elastic Search만을 도입했을 때, 검색 대상 데이터를 메인 인스턴스를 거쳐 전달하는 과정에서 힙 메모리 부족 현상 발생
- 데이터의 개수가 현재 메인 인스턴스가 전부를 감당하기에 무리이며, 트래픽이 몰리는 상황에서 데이터의 수정이 빈번하기 때문에 Elastic Search가 검색 대상 데이터를 수집할 수 있는 방법 마련 필요

<img width="662" alt="16" src="https://github.com/quokkaX2/classmate/assets/129869700/2cd095c4-39d6-40bd-abe6-9bb60eda01b3">

### ✔️ 해결

- 기술적 논의를 통해서 다양한 데이터 소스(JDBC, Kafka, Syslog 등)로부터 입력을 받아 필터를 거쳐 출력을 수행하도록 파이프라인을 구축하는 Log Stash 도입 결정

<img width="751" alt="17" src="https://github.com/quokkaX2/classmate/assets/129869700/3ebe3909-286e-4602-a4b5-4f54983edbe0">

- 또한, 검색 대상 데이터들이 Elastic Search에 온전히 색인되었는지 여부를 모니터링하기 위해서 Kibana를 추가로 도입, 이를 통해 ELK 체계를 구축할 수 있었음

![18](https://github.com/quokkaX2/classmate/assets/129869700/dfebdfa1-c7da-4093-a06d-fc24c4f5392c)

---

# 🛠️ 4. 서비스 아키텍처

![19](https://github.com/quokkaX2/classmate/assets/129869700/a903b5f6-e8bc-4772-8bb7-1ab600b197ec)


### 📂 ERD

![20](https://github.com/quokkaX2/classmate/assets/129869700/9c223ca8-94e4-41a8-a8ce-1c9f41139168)


---

# 🔧 5. 기술 스택

| 카테고리 | 사용 기술 |
| --- | --- |
| Backend | JAVA, Spring Boot, Spring JPA, Spring Security |
| TEST | JMeter, JUnit5, Postman |
| CI/CD | Github Actions, AWS S3, AWS CodeDeploy |
| Database | AWS RDS(MySQL), Redis |
| APM | Prometheus, Grafana |
| Search engines | Elastic Search, Kibana, Log Stash |
| DevOps | AWS EC2, AWS Application Load Balancer, AWS Auto Scaling, Docker, Nginx, Google Cloud Platform |

---

# ✔️ 6. 기술적 의사결정

| 요구사항 | 선택 기술 | 기술 선택 이유 |
| --- | --- | --- |
| 🪣 데이터 베이스 | **MySQL** vs PostgreSQL | MySQL은 PostgreSQL보다 읽기 성능에 강점을 가지고 있습니다. 복잡한 쿼리가 없고 쓰기 작업 보단 읽기 작업이 더 많은 현재 프로젝트에 적합한 MySQL을 선택하였습니다. |
| 📈 부하 테스트 | **Jmeter** vs nGrinder | nGrinder의 가장 큰 단점이 현재까지의 default 지원 자바 버전이 여전히 8이며, 최대 Java 11까지 지원되기 때문에 현재 프로젝트의 기반 프로그래밍 언어인 Java 17에 호환되는 JMeter를 선택하였습니다. |
| 🖥️ 모니터링 | **Prometheus, Grafana** vs Pinpoint | Spring boot에서 내보내는 메트릭들을 프로메테우스로 손쉽게 수집할 수 있고, 그라파나 대시보드를 import 해오면 주요 지표 시각화에 적은 시간이 들게 됩니다. 또한 오픈소스로 무료로 이용 가능하기 때문에 선택하였습니다. |
| ⚡️ 응답 속도, TPS 개선 | **Redis** vs Memcached | Memcached는 string 데이터 타입만 지원합니다. 반면에 Redis는 hash, set, list, string 등 다양한 데이터 구조를 지원합니다. Redis는 In Memory 데이터 저장소로서, 디스크 기반의 데이터 접근보다 데이터 I/O 속도가 빠릅니다. 반복적인 읽기에 대한 응답 속도 개선과 DB 부하를 줄일 수 있기에 선택하였습니다. |
| 🔍 검색 성능 개선 | **Elastic search, Log Stash, Kibana** vs Native Query | Elastic search는 텍스트를 토큰 단위로 분리, 분석하는 루씬 엔진과 http 메소드를 기반으로 운영 체제에 구애받지 않는 검색 환경을 제공하는 외부 라이브러리이며 데이터 로그 추적 및 수집을 맡는 Log Stash 및 검색 데이터 분석 및 모니터링을 맡는 Kibana를 통한 ELK 체계를 통해 빠르면서도 다양한 검색 조건을 제시하고 확장성을 고려할 수 있어서 선택하였습니다. |
| ⚙️ CI/CD | **Github Actions** vs Jenkins | Jenkins보다 설정이 단순하고, CI 서버 구축이 따로 필요 없으며 GitHub와 연동이 용이하며 사용 경험이 있는 GitHub Actions를 선택하였습니다. |
| 💬 소켓 통신 | **Web socket** vs SSE vs WebFlux | SSE에 비해 연결 제한이 없고 러닝커브가 낮으며 프로젝트 기간을 고려하였을 때, 사용 경험이 있는 WebSocket을 선택하였습니다. |
| 🥔DB 복제 | **Replication** vs Sharding | 초기에는 DB 복제와 샤딩 모두 적용하고자 하였습니다. 하지만, 초기 ERD 구성이 샤딩을 고려하지 않았기 때문에 복제를 선택하여 적용하게 되었습니다. |
