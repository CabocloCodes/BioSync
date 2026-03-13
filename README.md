# BioSync - Produtividade Adaptativa

App Android de produtividade que adapta sugestões de tarefas com base no seu nível de energia atual.

## Funcionalidades

- **Seletor de Energia**: Pergunta "Como está sua energia agora?" e filtra tarefas adequadas
- **Cadastro de Tarefas**: Com tags, data de entrega e nível de esforço mental (Baixo, Médio, Alto)
- **Sugestões Inteligentes**: Filtra tarefas com base no nível de energia selecionado
- **Estatísticas**: Gráfico de tarefas concluídas por nível de esforço
- **Notificações Inteligentes**: Lembretes de prazo e sugestões baseadas no horário do dia

## Tech Stack

- **Kotlin** + **Jetpack Compose**
- **Room** (banco de dados local)
- **Hilt** (injeção de dependência)
- **WorkManager** (notificações periódicas)
- **Material 3** com tema escuro customizado (azul e violeta)
- **Navigation Compose**

## Design

- Modo escuro por padrão
- Paleta de cores suaves: azul (#7B9CFF) e violeta (#B07BFF)
- Interface minimalista sem distrações
- Indicadores visuais de esforço mental (verde, âmbar, vermelho)

## Arquitetura

- MVVM (Model-View-ViewModel)
- Repository Pattern
- Single Activity com Navigation Compose

## Build

```bash
./gradlew assembleDebug
```

## Requisitos

- Android SDK 26+ (Android 8.0)
- Target SDK 34 (Android 14)
- Kotlin 1.9.22
- Gradle 8.5
