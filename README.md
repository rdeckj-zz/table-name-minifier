# table-name-minifier

A Clojure library designed to shorten SQL table names.

## Installation

1. Download the [latest release](https://github.com/rdeckj/table-name-minifier/releases)
2. Unzip the release folder, it contains the `tnmin` bash script and `tmin.jar`
2. Place them in the same directory somewhere in your `$PATH`
3. Run it

## Usage
A quick TLDR:

```
$ tnmin long_table_name_that_should_become_minified        # lng_tbl_nm_tht_shld_bcm_mnfd
$ tnmin short_table_name                                   # short_table_name
$ tnmin pound_remains_special_in_long_names                # lb_rmns_spcl_n_lng_nms
$ tnmin pound_is_special                                   # lb_is_special
$ tnmin --max 10 pound_is_special                          # lb_s_spcl
```

## Commands

### ```--max <number>```

Set the maximum length of the table name. Input over this length will be minified. Words that can be substituted for abbreviations are still replaced even if the input is under the specified length.

### --help
Print the help screen.

## License

Copyright © 2019 Joshua Radecki

Distributed under the Eclipse Public License.
