(defproject baseball "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.xml "0.2.0-alpha6"]
                 [hickory "0.7.1"]
                 [org.clojars.bfollek/rabbithole "0.2.2-SNAPSHOT"]]
  :repl-options {:init-ns baseball.core})
