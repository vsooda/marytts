i`marytts-builder`:  
 viewimport -> DatabaseImportMain -> viewImportComponent:(and child class)

----------------------
## raw acoustic

### praat: praatPitchMarker.java
`lib`: install by apt-get. praat package.  
`input`: wav  
`output dir`: data/

```
praat /home/research/data/tts/cmu/temp/script.praat /home/research/data/tts/cmu/basenames.lst /home/research/data/tts/cmu/wav/ /home/research/data/tts/cmu/pm/ 100 500
```

where script.praat is a script command.   
praat is a lib for pitch marks

### MCEPMarker: MCEPMarker.java -> ESTcaller.java


`dst`: generator pitchmarks, lpc, mel_cepstream  
`input`: pm files  
`output dir`: data/mcep/

hamming windows and so on
 
```
/usr/bin/sig2fv -window_type hamming -factor 2.5 -otype est_binary -coefs melcep -melcep_order 12 -fbank_order 24 -shift 0.01 -preemph 0.97 -pm /home/research/data/tts/cmu/pm/arctic_a0001.pm -o /home/research/data/tts/cmu/mcep/arctic_a0001.mcep /home/research/data/tts/cmu/wav/arctic_a0001.wav
```

-----------------
## transcripts conversion

data format transfer


-------------------------
## automic labeling
### allophonesExtractor 音位变体 
AllophonesExtractor.java
request the server. it need to do a lot of nlp tagger.  
`dst`: For the given texts, compute allophones, especially boundary tags.  
`output`: prompt_allophones  
set the request type and then call MaryHttpClient.java:_precess() --> requestInputStream() function to get the result then output to file.

### ehmmlabel
using the allophones result to do labling.
label the voice. they are all precessing the data/lab/ files.

### htklabel


### labelPauseDeleter
process the label file and remove the continue 'pau'


### labelledFilesInspector
`dst`: For the given texts, compute unit features and align them with the given unit labels.    
  
It offer manully label tool which can select several token and its voice. If push the save button, it will save the correspond pm, wav, lab file in the temp folder.  
It offer the method to play a selected section, or save the selection.



----------------------------------
## label-transcript alignment
### PhoneUnitLabelComputer
Convert phone labels to unit labels  
`output dir`: data/phonelab


### HalfPhoneUnitLabelComputer
energy peak difference..
it will create the data/halfphonelab/

###transcriptionAligner
transcriptionAligner.java -> MaryTranscriptionAligner.java  
`dst`: align and change automatic transcriptions to manually corrected ones  
this will create the allophones folder. how to do the alignment, not very sure.




------------------------
## feature extraction
### FeatureSelection
get the features-discrete and save to data/mary/features.txt


request from server. and the server precess it with InfoRequestHandler.java


### PhoneUnitFeatureComputer
 extract context feature vectors from the text data. This procedure will create a phonefeatures directory

---

## verify alignment
### phoneLabelFeatureAligner
verify alignment between "phonefeatures" and "phonelabels".


-------------
## basic data files
timelinemaker for what?


----

## HMM
### dataprepare
HMMVoiceDataPrepare  
only to prepare the data and set up the environment


### HMMVoiceConfigure : parameter configs.


### HMMVoiceFeatureSelection
 reads the mary/features.txt file (created in step 6), and generates the file mary/hmmFeatures.txt.  
 The hmmFeatures.txt file contains extra features, apart from phone and phonological features, that will be used to train HMMs. normally it is a subset of features.txt


### HMMVoiceMakeData:
call the hts to 'make data':make mgc lf0 str-mary cmp-mary list scp 


HMMVoiceMakeData.makeLabels(): get label and mlf files.
HMMVoiceMakeData.makeQuestions(): get questions files.


### HMMVoiceMakeVoice 
hts train.


```
log in the marytts/log.  server run the marytts.runtime. it load a lot of module to process the text or voices. such as marytts-runtime/marytts.modules.OpenNLPPostTagger.java to do POS tagger.
```




----------------------------

