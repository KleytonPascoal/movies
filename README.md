# Movies

Uma aplicação Android cliente para OMDb API - The Open Movie Database

## Getting Started

### Prerequisites

Java 8 e Android SDK setados no path da máquina.


### Product flavors:

1) mock: a ser usado para execução dos testes. Ele simula as chamadas à OMDb API e a presença ou não de conectividade com a internet.

2) prod: versão de produção, possui a integração à OMDb API.


### Testes:

A aplicação possui dos grupos de testes:

1) Módulo app: testes a serem aplicados afim de validar o comportamento de cada tela do app, analisando views, campos, intents lançados etc. Os testes podem ser encontrados na pasta androidTests. Seguindos algumas das práticas de [TDD](https://en.wikipedia.org/wiki/Test-driven_development)

2) Módule features-tests: testes baseados em features do sistema. Seguindo as práticas de [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development).


Exemplo da feature testada:

```
Feature: On My Movies page show saved movies
    As user of Movies App I want see my movies as carousel, as list and as grid.

    @ScenarioId("F01")
    Scenario: Show my movies saved as carousel, as list and as grid
        Given I see My Movies page - F01
        When  I select navigation bottom to show movie as carousel
        Then  I want to see my movies saved as carousel
        When  I select navigation bottom to show movie as list
        Then  I want to see my movies saved as list
        When  I select navigation bottom to show movie as grid
        Then  I want to see my movies saved as grid
        When  I swipe to right my movies grid
        Then  I want to see my movies as list again
        When  I swipe to right my movies list
        Then  I want to see my movies as carousel again

```



### App do ambiente de testes

A realização dos testes deve ser realizada em um dispositivo Android ou Emulador.

Execução dos testes de regressão das UI:

1) Vá até a pasta raíz do projeto.


2) Build o app com o comando abaixo:

```
./gradlew assembleMock
```

3) Agora instale o app:

```
./gradlew installMockDebugAndroidTest
```

4) Execute os testes no dispositivo:

```
./gradlew app:connectedMockDebugAndroidTest -Pandroid.testInstrumentationRunnerArgumeMockDebunts.class=com.kleytonpascoal.movies.test.suite.UIRegressionTestSuite
```

5) Após a execução os resultados dos testes poderão ser vistos em:

```
/<your-path>/app/build/reports/androidTests/connected/flavors/MOCK/index.html
```


6) Agora para testar a feature F01 citada acima, execute:

```
./gradlew features-tests:connectedCheck
```

7) Após a execução os resultados dos testes poderão ser vistos em:

```
/<your-path>/features-tests/build/reports/androidTests/connected/index.html
```

8) Após finalizar todos os testes desinstale o app:

```
./gradlew uninstallAll
```


### App do ambiente de produção

1) Para realizar o build da aplicação do ambiente de produção execute o camando abaixo:

```
./gradlew assembleProdDebug
```

2) Agora realiza a instalação:

```
./gradlew installProdDebug
```

3) Após uso para remover o app execute:

```
./gradlew uninstallAll
```

Enjoy ;)

