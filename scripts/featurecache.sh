#!/bin/bash
mkdir -p cache
if [ -d hts/data/mgc ]
then
    rm -rf cache/mgc
    mv hts/data/mgc cache/mgc
else
    echo "hts/data/mgc doesn't exist"
fi

if [ -d hts/data/lf0 ]
then
    rm -rf cache/lf0
    mv hts/data/lf0 cache/lf0
else
    echo "hts/data/lf0 doesn't exist"
fi

if [ -d hts/data/str ]
then
    rm -rf cache/str
    mv hts/data/str cache/str
else
    echo "hts/data/str doesn't exist"
fi

#rm -rf !(text|wav|cache|wav0|lab)
#rm -rf !(text|wav|cache|wav0|lab|clean.sh)
