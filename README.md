# rx-beers
A simple example that demonstrates the usage of RxJava to populate a list of items with some refresh logic.

Requirements:
- fetch data from remote source, store copy in local
- on the app resume check if the first page of items has changed 
- show Snackbar with a message that new data available

How to setup:
- run local web server with mocks provided in [mocks directory](mocks/)
- put server address in [BeersApp](app/src/main/java/pl/kitek/beers/presentation/BeersApp.kt#L27) class

Demo:

<img src="https://drive.google.com/uc?export=download&id=19D-GTEiW9Iwn0CcMKy7jceGdBkvOmOks" width="300"/>
