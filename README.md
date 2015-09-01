# Algo: Tree comparer

[![Build Status][travis-shield]][travis-link]
[![Coverage Status][coveralls-shield]][coveralls-link]
[![Coverity status][coverity-shield]][coverity-link]
[![License][license-shield]][license-link]

## Goal

This algorithm was designed to provide a way to easily compare several trees.

The provided implementation was more specifically designed to compare file trees, and was to be used
to compare the configurations of several environments (such as development, test, production) for a
professional project.

## TODO

* [ ] Make a module dedicated to reporting
* [ ] Remove the binaries from the HTML reports
* [ ] Remove the ``Main`` class from the comparer-file method
* [ ] Use Freemarker for the reporting
* [ ] Javadoc
* [ ] Review code coverage

[travis-shield]: http://img.shields.io/travis/cyChop/tree-comparer/master.svg
[travis-link]: https://travis-ci.org/cyChop/tree-comparer
[coveralls-shield]: http://img.shields.io/coveralls/cyChop/tree-comparer/master.svg
[coveralls-link]: https://coveralls.io/r/cyChop/tree-comparer?branch=master
[coverity-shield]: https://img.shields.io/coverity/scan/6219.svg
[coverity-link]: https://scan.coverity.com/projects/cychop-tree-comparer
[license-shield]: https://img.shields.io/badge/license-ASF%202.0-blue.svg
[license-link]: http://www.apache.org/licenses/LICENSE-2.0
