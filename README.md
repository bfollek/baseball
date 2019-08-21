# baseball

Fetch and reshape a bunch of major league baseball game data. Adapted from the rust code in

* https://tht.fangraphs.com/baseball-coding-with-rust-intro/ - Done
* https://tht.fangraphs.com/baseball-coding-with-rust-part-2/ - Done
* https://tht.fangraphs.com/baseball-coding-with-rust-part-3/

## Benchmarking

linescore-parse retrieves an URL; linescores calls it with 15 different urls. This seems like a good test case for map vs. pmap.

Rudimentary benchmarking makes pmap look a consistent order of magnitude faster than map. (I'm assuming the first map result is some sort of initialization outlier.)

```
baseball.core=> (def x (time (doall (linescores games map))))
"Elapsed time: 5441.736059 msecs"
#'baseball.core/x

baseball.core=> (def x (time (doall (linescores games pmap))))
"Elapsed time: 46.62959 msecs"
#'baseball.core/x

baseball.core=> (def x (time (doall (linescores games map))))
"Elapsed time: 320.786797 msecs"
#'baseball.core/x

baseball.core=> (def x (time (doall (linescores games pmap))))
"Elapsed time: 40.223559 msecs"
#'baseball.core/x

baseball.core=> (def x (time (doall (linescores games map))))
"Elapsed time: 225.619857 msecs"
#'baseball.core/x

baseball.core=> (def x (time (doall (linescores games pmap))))
"Elapsed time: 63.672544 msecs"
#'baseball.core/x
```

I also wanted to benchmark with [criterium](https://github.com/hugoduncan/criterium), but the first time I tried to quick-bench the pmap version, my Mac crashed. criterium bug? clojure bug? jvm bug? OS X bug? I dunno. Maybe I'll poke around a bit when I have a chance, and see if I can shorten the list of suspects.

## Legal

Copyright © 2019 Robert I. Follek

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
