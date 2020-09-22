# movie-flow
Demonstration project showcasing Kotlin's Flow APIs


Note:
1. this is a quick demonstration project, as such it does not have dependency injection and other production-ready setup, tests, etc.
2. some alpha and non-production-ready dependencies are used (such as AndroidX DataStore, KotlinX)
3. the app is missing some UI edge-cases such as restoring state of input and the toggle
4. http://www.omdbapi.com/ API is used to fetch data, the API key is currently hardcoded and limited to 1000 calls per day, you should overwrite it with your own free key
