# Arf-ly - AI·IoT 기반 반려동물 헬스케어 플랫폼

<br>

<p align="center">
  <img src="assets/banner.png" width="800"/>
</p>

<br>

## 프로젝트 기간

**2025년 03월 ~ 진행 중**

## 배포

**[Arf-ly 접속하기](https://arf-ly-web.vercel.app/)**

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

## 서비스 화면

### 로그인

<table width="100%">
  <tr>
    <td align="left"><img src="assets/시작화면.png" height="350"/></td>
    <td align="right"><img src="assets/로그인.PNG" height="350"/></td>
  </tr>
  <tr>
    <td align="center">시작 화면</td>
    <td align="center">일반 로그인</td>
  </tr>
</table>

### 메인

<table width="100%">
  <tr>
    <td align="left"><img src="assets/메인화면.PNG" width="228" height="350"/></td>
    <td align="right"><img src="assets/메인화면%20ai진단결과.PNG" width="228" height="350"/></td>
  </tr>
  <tr>
    <td align="center">메인 화면</td>
    <td align="center">AI 진단 결과 리스트</td>
  </tr>
</table>

### AI 피부 질환 진단

<table width="100%">
  <tr>
    <td align="left"><img src="assets/ai 피부진단 동물 선택.PNG" height="350"/></td>
    <td align="right"><img src="assets/ai 피부진단 결과 리포트.PNG" height="350"/></td>
  </tr>
  <tr>
    <td align="center">동물 선택</td>
    <td align="center">진단 결과 리포트</td>
  </tr>
</table>

### 산책 대시보드

<table width="100%">
  <tr>
    <td align="left"><img src="assets/산책 대시보드_활동량.PNG" height="350"/></td>
    <td align="right"><img src="assets/산책 대시보드_산책거리 2.PNG" height="350"/></td>
  </tr>
  <tr>
    <td align="center">활동점수</td>
    <td align="center">산책 거리</td>
  </tr>
</table>

### 동물병원 지도

<table width="100%">
  <tr>
    <td align="left"><img src="assets/동물지도 병원_리스트.PNG" height="350"/></td>
    <td align="right"><img src="assets/동물지도 병원_상세정보.PNG" height="350"/></td>
  </tr>
  <tr>
    <td align="center">동물병원 리스트</td>
    <td align="center">동물병원 상세 정보</td>
  </tr>
</table>

### 커뮤니티

<table width="100%">
  <tr>
    <td align="left"><img src="assets/커뮤니티 리스트.PNG" height="350"/></td>
    <td align="right"><img src="assets/커뮤니티 상세 정보.PNG" height="350"/></td>
  </tr>
  <tr>
    <td align="center">커뮤니티 리스트</td>
    <td align="center">커뮤니티 상세 정보</td>
  </tr>
</table>

### 마이페이지

<table width="100%">
  <tr>
    <td align="left"><img src="assets/마이페이지.PNG" height="350"/></td>
    <td align="right"><img src="assets/마이페이지 복약 알림_서비스.PNG" height="350"/></td>
  </tr>
  <tr>
    <td align="center">마이페이지</td>
    <td align="center">복약 알림 서비스</td>
  </tr>
</table>

## 기술 스택

**Backend**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)

**AI**

![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![TensorFlow](https://img.shields.io/badge/TensorFlow-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)

**Frontend**

![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)

**IoT**

![Raspberry Pi](https://img.shields.io/badge/Raspberry_Pi-C51A4A?style=for-the-badge&logo=raspberry-pi&logoColor=white)

**Infra**

![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![NGINX](https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=nginx&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)

**문서 작성**

![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![한컴독스](https://img.shields.io/badge/한컴독스-005BAC?style=for-the-badge&logoColor=white)
![Google Sheets](https://img.shields.io/badge/Google_Sheets-34A853?style=for-the-badge&logo=google-sheets&logoColor=white)

**협업 툴**

![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)

**디자인 및 설계**

![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)

**버전 관리**

![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

## ERD

<p align="center">
  <img src="assets/erd.png" width="800"/>
</p>

## 아키텍처

<p align="center">
  <img src="assets/architecture.png" width="800"/>
</p>

## 프로젝트 파일 구조

<details>
<summary><b>Backend</b></summary>

<pre>
src
  └─ main
       └─ java
            └─ com.capstone.arfly
                 ├─ ad
                 │    └─ domain
                 ├─ common
                 │    ├─ auth
                 │    ├─ config
                 │    ├─ constant
                 │    ├─ domain
                 │    ├─ dto
                 │    ├─ exception
                 │    └─ util
                 ├─ community
                 │    ├─ controller
                 │    ├─ domain
                 │    ├─ dto
                 │    ├─ repository
                 │    └─ service
                 ├─ diagnosis
                 │    ├─ controller
                 │    ├─ domain
                 │    ├─ dto
                 │    ├─ repository
                 │    └─ service
                 ├─ hospital
                 │    ├─ controller
                 │    ├─ dto
                 │    └─ service
                 ├─ iot
                 │    ├─ controller
                 │    ├─ domain
                 │    ├─ dto
                 │    ├─ repository
                 │    └─ service
                 ├─ member
                 │    ├─ controller
                 │    ├─ domain
                 │    ├─ dto
                 │    ├─ repository
                 │    └─ service
                 ├─ notification
                 │    ├─ controller
                 │    ├─ domain
                 │    ├─ dto
                 │    ├─ repository
                 │    └─ service
                 ├─ pet
                 │    ├─ controller
                 │    ├─ domain
                 │    ├─ dto
                 │    ├─ repository
                 │    └─ service
                 └─ walkRecord
                      ├─ controller
                      ├─ domain
                      ├─ dto
                      ├─ repository
                      └─ service
</pre>

</details>

<details>
<summary><b>Frontend</b></summary>

<pre>
src
  ├─ assets
  │    ├─ bottom_tab_bar
  │    ├─ community
  │    ├─ home
  │    │    ├─ Camera
  │    │    ├─ diseaseCheck
  │    │    ├─ walkedResult
  │    │    └─ weeklyActive
  │    ├─ login
  │    │    ├─ social
  │    │    └─ system
  │    ├─ map
  │    ├─ mypage
  │    │    ├─ IotRegistration
  │    │    ├─ MedicineAlarm
  │    │    ├─ PetDetail
  │    │    └─ UserProfile
  │    ├─ pet
  │    │    └─ register
  │    └─ terms
  ├─ components
  │    └─ BottomTabBar
  ├─ pages
  │    ├─ auth
  │    │    ├─ Find
  │    │    ├─ Login
  │    │    └─ Signup
  │    ├─ community
  │    ├─ home
  │    │    └─ data
  │    ├─ map
  │    ├─ mypage
  │    │    ├─ data
  │    │    └─ IotRegisteration
  │    └─ pet
  ├─ style
  ├─ App.jsx
  ├─ firebase.js
  ├─ index.css
  └─ main.jsx
</pre>

</details>
