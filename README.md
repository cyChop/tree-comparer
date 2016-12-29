# Tree comparer

[travis-badge]: https://img.shields.io/travis/cyChop/tree-comparer.svg
[travis]: https://travis-ci.org/cyChop/tree-comparer
[sonarc-badge]: https://img.shields.io/sonar/https/sonarqube.com/org.keyboardplaying:tree-comparer/coverage.svg
[sonarc]: https://sonarqube.com/overview/coverage?id=org.keyboardplaying:tree-comparer
[sonarq-badge]: https://img.shields.io/sonar/https/sonarqube.com/org.keyboardplaying:tree-comparer/tech_debt.svg
[sonarq]: https://sonarqube.com/overview/debt?id=org.keyboardplaying:tree-comparer
[issues-badge]: https://img.shields.io/github/issues-raw/cyChop/tree-comparer.svg
[issues]: https://github.com/cyChop/tree-comparer/issues
[waffle]: https://waffle.io/cyChop/tree-comparer
[licens-badge]: https://img.shields.io/github/license/cyChop/tree-comparer.svg
[licens]: http://www.apache.org/licenses/LICENSE-2.0

[![Build status][travis-badge]][travis]
[![Test coverage][sonarc-badge]][sonarc]
[![Technical debt][sonarq-badge]][sonarq]
[![Issues][issues-badge]][issues]
[![License: Apache 2.0][licens-badge]][licens]

## What's this?

This project is an algorithm I designed to compare two or more trees.
The algorithm is designed so that it can be applied to any kind of tree (understand: a node with children nodes, with children nodes, ...).

The most immediate application is obviously the comparison of directories.
This is why the project already contains an implementation for files.
It was proofed by (accurately) comparing the configuration of several environments (local, development, quality & acceptance, production).

## What's in this?

- `tree-comparer-model`: the objects needed for the use of the algorithm.
- `tree-comparer-algorithm`: the comparison algorithm properly.
- `tree-comparer-reporter`: a tool to generate a report from a compared tree.
- `tree-comparer-file`: the implementation of the models and utilities for directory comparison.
- `plaintext-diff`: a plaintext comparison utility, extracted from the [ASF 2.0 licensed](https://www.apache.org/licenses/LICENSE-2.0) [Diff Match Patch project](https://code.google.com/p/google-diff-match-patch/); used mainly for reporting.

== The concurrence

Some products already exist and are easier to use, with an interactive user interface.

- [WinMerge](http://winmerge.org/) can compare two directories and enables interactive browsing of the differences.
- [KDiff3](http://kdiff3.sourceforge.net/) can compare up to three directories.

This algorithm however can compare a virtually unlimited number of trees.
