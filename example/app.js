// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});
win.open();

var onRecording=false;
var onPlaying=false;

// TODO: write your module tests here
var voicerecorder = require('com.zookee.voicerecorder');
	var view1=Ti.UI.createLabel({
		text:'start',
		left:100,
		width:100,
		top:10,
		height:50,
		backgroundColor:'blue'
	});		
	
	var view3=Ti.UI.createLabel({
		text:'play',
		left:100,
		top:250,
		height:50,
		width:100,
		backgroundColor:'red'
	});

	var proxy = voicerecorder.createRecorder();
	var file = Ti.Filesystem.getFile(Ti.Filesystem.externalStorageDirectory,'MyVoice2000.3gp');
	proxy.recordFile=file.nativePath.slice(7);
	proxy.maxDuration=50000;
	var durationReachedCB = function(){
		onRecording=false;
		view1.text='RecordRRR';
	}
	
	var playCompleteCB = function(){
		onPlaying=false;
		view3.text='playyy';
	}
	view1.addEventListener('click',function(e){
		if(onRecording){
			proxy.stopRecord();
			onRecording=false;
			view1.text='Record';
		}
		else{
			proxy.startRecord({
				durationReached:durationReachedCB
			});
			onRecording=true;
			view1.text='stop';
		}
	});
	
	view3.addEventListener('click',function(e){
		if(onPlaying){
			proxy.pause();
			onPlaying=false;
			view3.text='Play';
		}else{
			proxy.startPlaying({
				playCompleted:playCompleteCB
			});
			onPlaying=true;
			view3.text='Stop';
		}
	});
	win.add(view1);
	win.add(view3);
	

