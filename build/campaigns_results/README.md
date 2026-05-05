# CPU / Cache / Mixed campaign results

This folder contains a lightweight summary of the Grid5000 campaign results for the second benchmark scenario.

The full raw campaign archive is not committed because it contains large JMH and Kwollect raw files. The committed CSV aggregates the `version-summary.json` files for the three workload categories:

- `CPU`: insertion and deletion workloads.
- `Cache`: search and data-access workloads.
- `Mixed`: traversal and aggregation workloads.

All measurements use the same Eclipse Collections versions as the main campaign and the Grid5000/Kwollect metric `wattmetre_power_watt`.

Main file:

- `ec_cpu_cache_mixed_summary.csv`: one row per category and Eclipse Collections version, including execution time, JMH score, average power, idle power, gross energy and net energy.
