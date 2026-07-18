# SecureVault: Zero-Knowledge Password Manager

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&style=flat-square)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-green?logo=spring&style=flat-square)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue-3.5-emerald?logo=vue.js&style=flat-square)](https://vuejs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql&style=flat-square)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](./LICENSE)

Полноценное full-stack приложение для безопасного хранения учетных данных, построенное по архитектуре **Zero-Knowledge**. Сервер никогда не хранит и не видит пароли в открытом виде: все чувствительные данные шифруются с использованием AES-256-GCM, а ключи деривируются через PBKDF2.

---

## Технологический стек

| Категория | Технологии |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.3, Spring Security (JWT), Spring Data JPA |
| **Frontend** | Vue 3 (Composition API), TypeScript, Vite, TailwindCSS, Axios |
| **Database** | PostgreSQL 16, Hibernate ORM |
| **Security** | AES-256-GCM, PBKDF2, BCrypt |
| **DevOps** | Git, Docker, Ansible, k3s *(в процессе внедрения)* |

---

## Архитектура и Безопасность

Проект реализует строгие принципы безопасности, что делает его отличным примером для демонстрации навыков работы с криптографией и защищенной архитектурой:

1. **Zero-Knowledge подход:** Сервер не знает мастер-ключ пользователя и не может расшифровать данные без явной авторизации сессии.
2. **Шифрование данных:** Логины и пароли сервисов шифруются алгоритмом **AES-256-GCM** (обеспечивает и конфиденциальность, и аутентичность данных).
3. **Деривация ключей:** Ключ шифрования генерируется из мастер-ключа через **PBKDF2** (100 000 итераций) с уникальной солью для каждого пользователя.
4. **Управление сессиями:** Реализован механизм явной разблокировки хранилища (`/api/passwords/unlock`). Криптографический ключ хранится **только в оперативной памяти** активной сессии и никогда не логируется или не сохраняется на диск.
5. **Хэширование паролей:** Пароли пользователей для аутентификации хэшируются через **BCrypt** с автоматической генерацией соли.

---

## Быстрый старт

📖 Полная пошаговая инструкция по настройке окружения, переменных и локальному запуску вынесена в отдельный файл:  
👉 **[⚙️ Инструкция по локальному запуску (SETUP.md)](./SETUP.md)**

---

## Roadmap

- [x] Базовая аутентификация и CRUD-операции с паролями
- [x] Миграция с локального JSON-хранения на PostgreSQL и Hibernate
- [x] Реализация ядра Zero-Knowledge шифрования (AES-256-GCM + PBKDF2)
- [x] Вынос конфигурации и секретов в переменные окружения (12-Factor App)
- [ ] Написание Multi-stage Dockerfile для Backend и Frontend
- [ ] Настройка Docker Compose для локального развертывания всего стека
- [ ] Деплой на VPS (k3s) с использованием Ansible и Terraform
- [ ] Перенос шифрования на клиентскую сторону (Web Crypto API) для абсолютного Zero-Knowledge

---

## История изменений

Полная хронология развития проекта, архитектурных миграций и исправленных багов задокументирована здесь:  
**[UPDATES.md](./UPDATES.md)**

---

## Credits & Original Repository

Этот проект является развитием и масштабным рефакторингом оригинального репозитория:  
**[ayecy/password-manager](https://github.com/ayecy/password-manager)**

**Ключевой вклад контрибьютора [@ImaEynD](https://github.com/ImaEynD):**
- Полная миграция архитектуры хранения данных с JSON на реляционную СУБД PostgreSQL.
- Настройка профессиональной структуры проекта, `.gitignore`, `.dockerignore` и документации.
- Рефакторинг конфигурации под стандарты 12-Factor App и исправление критических багов интеграции.

---

## Автор и Контакты

Разработано с упором на безопасность данных и современные стандарты инженерии. Буду рад обратной связи и обсуждению архитектурных решений!

| Ресурс | Ссылка |
| :--- | :--- |
| **GitHub** | [@ImaEynD](https://github.com/ImaEynD) |
| **Telegram** | [Написать мне](https://t.me/eynd0) |

>*Если вы нашли этот проект полезным, не забудьте поставить ⭐️ репозиторию!*