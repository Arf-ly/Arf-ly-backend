# Arf-ly - AI·IoT 기반 반려동물 헬스케어 플랫폼

<br>

<p align="center">
  <img src="assets/banner.png" width="800"/>
</p>

<br>

## 목차

1. [팀 구성](#팀-구성)
2. [서비스 소개](#서비스-소개)
3. [핵심 기능](#핵심-기능)
4. [기술 스택](#기술-스택)
5. [ERD](#erd)
6. [아키텍처](#아키텍처)
7. [향후 개발 계획](#향후-개발-계획)
8. [개발 문서](#개발-문서)

## 팀 구성

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/SHNAME">
        <img src="https://github.com/SHNAME.png" width="100" height="100" style="border-radius:50%"/><br/>
         이시형<br/>(Back-End/팀장)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/jangseheon">
        <img src="https://github.com/jangseheon.png" width="100" height="100" style="border-radius:50%"/><br/>
        장세헌<br/>(Back-End)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kang-bs">
        <img src="https://github.com/kang-bs.png" width="100" height="100" style="border-radius:50%"/><br/>
        강보성<br/>(Back-End)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kayeong97">
        <img src="https://github.com/kayeong97.png" width="100" height="100" style="border-radius:50%"/><br/>
        김가영<br/>(Front-End/AI)
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      인증 시스템<br/>소셜 로그인<br/>커뮤니티<br/>FCM 푸시 알림<br/>IoT 도메인·산책 기록
    </td>
    <td align="center">
      병원 조회<br/>AI 피부 진단<br/>진단 리포트·기록 조회
    </td>
    <td align="center">
      반려동물·회원 관리<br/>S3 파일 업로드<br/>IoT 기기 연결<br/>CI/CD
    </td>
    <td align="center">
      AI 진단 온보딩<br/>로그인·회원가입<br/>AI 모델 학습<br/>Python 서버 구축
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/wnwngud">
        <img src="https://github.com/wnwngud.png" width="100" height="100" style="border-radius:50%"/><br/>
        서주형<br/>(Front-End/IoT)
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/eegnim">
        <img src="https://github.com/eegnim.png" width="100" height="100" style="border-radius:50%"/><br/>
        김민지<br/>(Front-End)
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img src="assets/soyunProfile.jpg" width="100" height="100" style="border-radius:50%"/><br/>
        김소윤<br/>(Design)
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img src="assets/jeonghanProfile.png" width="100" height="100" style="border-radius:50%"/><br/>
        진정한<br/>(Design)
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      메인 페이지<br/>IoT 대시보드<br/>IoT HW·SW 개발
    </td>
    <td align="center">
      커뮤니티 페이지<br/>동물병원 지도 페이지
    </td>
    <td align="center">
      UI/UX 디자인
    </td>
    <td align="center">
      UI/UX 디자인
    </td>
  </tr>
</table>

## 서비스 소개

Arf-ly는 **AI 피부 질환 진단**과 **IoT 웨어러블 기기**를 결합한 반려동물 통합 헬스케어 플랫폼입니다.
반려동물의 피부 이상 증상을 사진 한 장으로 AI가 분석하고, IoT 기기로 산책 데이터를 자동 수집합니다.
피부 진단 · 산책 기록 · 동물병원 조회 · 커뮤니티를 하나의 서비스에서 제공합니다.

## 핵심 기능

| 기능          | 설명                                                       |
|-------------|----------------------------------------------------------|
| AI 피부 질환 진단 | 반려동물 피부 사진을 업로드하면 AI가 질환 가능성을 분석하고 맞춤형 관리 리포트를 제공        |
| IoT 산책 기록   | Raspberry Pi Pico W + GPS + 가속도 센서로 산책 중 활동량·이동 경로 자동 수집 |
| 반려동물 관리     | 반려동물 정보 등록 및 AI 진단·산책 기록을 통합 관리                          |
| 동물병원 조회     | 등록 주소 기반 주변 동물병원 목록 및 상세 정보 제공                           |
| 커뮤니티        | 반려동물 건강 관련 고민과 경험을 다른 보호자와 공유                            |

## 기술 스택

| 기술 | 설명 |
|---|---|
| Java, Spring Boot | 백엔드 서버 |
| PostgreSQL | 회원·반려동물·커뮤니티·진단 등 모든 도메인의 주 데이터베이스 |
| Redis | JWT Refresh Token 저장 및 만료 관리 / 커뮤니티 최근 검색어 캐싱 / 게시글 좋아요 수 캐싱 후 5분 주기 DB 동기화 |
| Firebase | FCM을 통한 복약 알림·댓글 멘션 푸시 알림 발송 / Firebase Auth 기반 전화번호 인증 |
| AWS S3 | 반려동물 프로필·게시글·AI 진단 이미지 및 영상 저장 |
| Spotless | palantir-java-format 기반 자동 코드 포맷팅 |
| Checkstyle | 런타임 버그로 이어질 수 있는 코드 패턴 검사 |
| Prometheus, Grafana | 애플리케이션 메트릭 수집 및 시각화 |
| Loki, Grafana Alloy | 로그 수집 및 검색 |
| mise | Java/Node/lefthook/Redis 등 개발 환경 버전 관리 |
| lefthook | Git 훅 매니저 |
| commitlint | 커밋 메시지 컨벤션 검증 |
| AWS EC2, Docker | 배포 서버 및 컨테이너 실행 환경 |
| GitHub Actions | CI/CD 파이프라인 |

## ERD

<p align="center">
  <img src="assets/erd.png" width="800"/>
</p>

## 아키텍처

<p align="center">
  <img src="assets/architecture.png" width="800"/>
</p>

## 향후 개발 계획

| 기능           | 설명                                          |
|--------------|---------------------------------------------|
| AI 모델 정확도 개선 | 모델 변경을 통한 피부 질환 진단 정확도 향상                   |
| 실시간 피부 진단    | Jetson Nano + 웹캠 기반 실시간 객체 탐지 및 피부 진단 기능 도입 |
| 관리자 기능      | 회원·콘텐츠·광고 관리 및 서비스 통계 조회 기능 제공               |

## 개발 문서

브랜치 전략, 커밋/코드 컨벤션, 폴더 구조, 개발 환경 세팅, 모니터링, 배포 구조 등 서버 개발 온보딩에 필요한 기술 문서는 [Wiki](https://github.com/Arf-ly/Arf-ly-backend/wiki)에서 확인할 수 있습니다.
