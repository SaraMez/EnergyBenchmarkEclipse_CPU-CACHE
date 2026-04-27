# EnergyBenchmarkEclipse_CPU

Projet JMH Eclipse Collections reorganise par profil de charge :

- `benchmark.cpu.CpuBenchmark` : insertions et suppressions
- `benchmark.cache.CacheBenchmark` : recherches, contains, get, detect
- `benchmark.mixed.MixedBenchmark` : parcours, reductions et forEach

Exemples :

```powershell
.\gradlew.bat jmh -PecVersion=13.0.0 -PjmhIncludes=benchmark.cpu.*
.\gradlew.bat jmh -PecVersion=13.0.0 -PjmhIncludes=benchmark.cache.*
.\gradlew.bat jmh -PecVersion=13.0.0 -PjmhIncludes=benchmark.mixed.*
```

Pour tout lancer :

```powershell
.\gradlew.bat jmh -PecVersion=13.0.0 -PjmhIncludes=benchmark.(cpu|cache|mixed).*
```
