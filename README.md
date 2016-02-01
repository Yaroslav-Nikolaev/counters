# counters
thread safe metrics

There are some implementations of counters and metrics(in future they will be separated).
All implementations of metrics have an error equal sum of the current and the last period(in current version it is seconds) of requested interval, which made deliberately.
