# 📸 API Casamento - Captura e Compartilhamento de Fotos ao Vivo

Uma aplicação Full-Stack desenvolvida para capturar momentos em tempo real durante o casamento da Isabella e do Márcio. Os convidados acessam a plataforma via QR Code, tiram fotos diretamente do navegador do celular e as imagens são enviadas instantaneamente para um álbum digital na nuvem.

## 🚀 O Problema que Resolvemos
Geralmente, em eventos, as fotos tiradas pelos convidados se perdem no WhatsApp ou exigem o download de aplicativos pesados. Esta solução oferece uma **PWA (Progressive Web App) leve**, acessível por qualquer navegador móvel (iOS e Android), com integração nativa à câmera do dispositivo e armazenamento seguro em nuvem.

## 🛠️ Tecnologias Utilizadas

**Back-End:**
* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) **Java 21**
* ![Spring Boot](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) **Spring Boot 3** (Web, JPA)

**Front-End:**
* ![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white) **HTML5 & CSS3** (Design "Dark Wedding" responsivo e com Glassmorphism)
* ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) **JavaScript Vanilla (Fetch API)** para requisições assíncronas.

**Infraestrutura e Banco de Dados:**
* ![PostgreSQL](https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) **Neon Tech (PostgreSQL)** Serverless para armazenamento de dados e URLs.
* ![Firebase](https://img.shields.io/badge/firebase-%23039BE5.svg?style=for-the-badge&logo=firebase&logoColor=white) **Google Firebase Storage** para hospedagem dos arquivos físicos.
* ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) **Docker** para conteinerização da aplicação.
* ![Render](https://img.shields.io/badge/Render-%2346E3B7.svg?style=for-the-badge&logo=render&logoColor=white) **Render** para Deploy contínuo em nuvem.

## 🧠 Arquitetura do Sistema

1. **Client-Side:** O convidado acessa a interface via QR Code. O JS intercepta a foto e envia via `FormData` para o servidor, com feedback visual imediato bloqueando cliques duplos.
2. **Controller (Spring):** Recebe o arquivo em formato Multipart e delega para a camada de serviço.
3. **Nuvem:** A API se autentica no Google Cloud (Firebase) via Variáveis de Ambiente e faz o upload do arquivo binário, gerando uma URL pública.
4. **Persistência:** A URL gerada pelo Firebase é salva no banco relacional PostgreSQL (Neon).

## ⚙️ Como rodar o projeto localmente

### Pré-requisitos
* Java JDK 21+
* Maven
* Conta no Firebase e Neon Tech

### Passos:
1. Clone o repositório:
```bash
git clone [https://github.com/SEU-USUARIO/seu-repositorio.git](https://github.com/SEU-USUARIO/seu-repositorio.git)
```

2. Configure as variáveis de ambiente no seu arquivo `application.properties` com as suas credenciais do banco de dados Neon.

3. Adicione o arquivo de chaves do Firebase (`firebase-key.json`) na raiz ou configure a variável de ambiente `FIREBASE_JSON` com o conteúdo da chave.

4. Compile e rode o projeto:
```bash
mvn spring-boot:run
```

5. Acesse `http://localhost:8080` no seu navegador.
