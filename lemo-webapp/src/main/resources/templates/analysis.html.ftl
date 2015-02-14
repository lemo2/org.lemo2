<#import "layout.html.ftl" as layout> <@layout.page title=analysis.name
scriptBase="/"+analysis.path+"/assets" scripts=analysis.scriptPaths>

<h2>${analysis.name}</h2>

<div class="row">
	<div id="analysis-forms" class="col-md-3 col-md-push-9"></div>
	<div id="analysis-main" class="col-md-9 col-md-pull-3">
		<div id="viz" style="width: 100%;"></div>
	</div>
</div>


<script type="text/javascript">
	(function(d3custom, $, undefined) {
		var q2result = {
			"links" : [ {
				"source" : "0",
				"target" : "11",
				"value" : "5",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "12",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "14",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "15",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "16",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "17",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "18",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "19",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "1",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "2",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "3",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "4",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "5",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "6",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "7",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "8",
				"value" : "5",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "9",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "0",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "13",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "15",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "16",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "17",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "18",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "19",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "0",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "2",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "3",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "4",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "5",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "6",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "7",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "8",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "1",
				"target" : "10",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "14",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "16",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "17",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "18",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "0",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "1",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "3",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "4",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "5",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "7",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "8",
				"value" : "5",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "9",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "2",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "11",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "12",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "13",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "15",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "18",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "19",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "0",
				"value" : "5",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "1",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "2",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "4",
				"value" : "6",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "5",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "6",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "7",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "9",
				"value" : "5",
				"pathId" : 0
			}, {
				"source" : "3",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "11",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "14",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "16",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "17",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "18",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "0",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "1",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "2",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "3",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "5",
				"value" : "5",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "6",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "7",
				"value" : "5",
				"pathId" : 0
			}, {
				"source" : "4",
				"target" : "9",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "13",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "14",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "16",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "19",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "0",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "1",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "4",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "6",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "7",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "8",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "9",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "5",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "14",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "15",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "18",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "19",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "0",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "1",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "3",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "4",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "7",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "9",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "6",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "12",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "13",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "14",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "15",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "16",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "17",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "18",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "19",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "0",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "2",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "3",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "4",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "5",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "6",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "8",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "9",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "7",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "12",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "15",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "16",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "19",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "0",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "1",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "3",
				"value" : "6",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "4",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "5",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "6",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "7",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "9",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "8",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "12",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "13",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "14",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "16",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "17",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "18",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "0",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "1",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "3",
				"value" : "5",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "4",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "5",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "7",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "9",
				"target" : "8",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "12",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "14",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "16",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "17",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "18",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "19",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "0",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "1",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "2",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "4",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "5",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "6",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "7",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "10",
				"target" : "9",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "0",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "12",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "1",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "13",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "2",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "3",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "14",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "4",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "5",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "7",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "8",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "11",
				"target" : "9",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "0",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "1",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "13",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "3",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "14",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "15",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "5",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "8",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "9",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "12",
				"target" : "10",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "13",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "13",
				"target" : "0",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "13",
				"target" : "12",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "13",
				"target" : "1",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "13",
				"target" : "14",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "13",
				"target" : "17",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "13",
				"target" : "8",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "13",
				"target" : "9",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "13",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "15",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "16",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "17",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "18",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "0",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "1",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "2",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "3",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "4",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "5",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "6",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "7",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "8",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "9",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "14",
				"target" : "10",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "15",
				"target" : "1",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "15",
				"target" : "2",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "15",
				"target" : "5",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "15",
				"target" : "16",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "15",
				"target" : "7",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "15",
				"target" : "18",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "15",
				"target" : "19",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "15",
				"target" : "9",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "12",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "13",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "14",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "15",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "19",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "0",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "1",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "2",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "3",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "4",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "6",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "7",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "9",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "16",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "0",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "1",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "2",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "3",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "14",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "4",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "16",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "6",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "18",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "19",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "17",
				"target" : "9",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "11",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "12",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "2",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "3",
				"value" : "4",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "14",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "4",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "5",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "16",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "17",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "6",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "8",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "18",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "0",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "1",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "2",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "3",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "14",
				"value" : "3",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "4",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "7",
				"value" : "1",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "8",
				"value" : "2",
				"pathId" : 0
			}, {
				"source" : "19",
				"target" : "10",
				"value" : "1",
				"pathId" : 0
			} ],
			"nodes" : [ {
				"name" : "Assessment Assignment 1.3",
				"title" : null,
				"value" : 46,
				"group" : 1,
				"pathId" : 0,
				"type" : "Assignment",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Collaborative Chat 1.5",
				"title" : null,
				"value" : 42,
				"group" : 1,
				"pathId" : 0,
				"type" : "Chat",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Collaborative Forum 1.1",
				"title" : null,
				"value" : 29,
				"group" : 1,
				"pathId" : 0,
				"type" : "Forum",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Assessment Test 1.4",
				"title" : null,
				"value" : 49,
				"group" : 1,
				"pathId" : 0,
				"type" : "Test",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Collaborative Chat 1.2",
				"title" : null,
				"value" : 37,
				"group" : 1,
				"pathId" : 0,
				"type" : "Chat",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Video 1.4",
				"title" : null,
				"value" : 27,
				"group" : 1,
				"pathId" : 0,
				"type" : "Video",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Video 1.7",
				"title" : null,
				"value" : 24,
				"group" : 1,
				"pathId" : 0,
				"type" : "Video",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Assessment Feedback 1.5",
				"title" : null,
				"value" : 34,
				"group" : 1,
				"pathId" : 0,
				"type" : "Feedback",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Collaborative Forum 1.4",
				"title" : null,
				"value" : 38,
				"group" : 1,
				"pathId" : 0,
				"type" : "Forum",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Assessment Test 1.1",
				"title" : null,
				"value" : 32,
				"group" : 1,
				"pathId" : 0,
				"type" : "Test",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Document 1.6",
				"title" : null,
				"value" : 19,
				"group" : 1,
				"pathId" : 0,
				"type" : "Document",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Video 1.10",
				"title" : null,
				"value" : 20,
				"group" : 1,
				"pathId" : 0,
				"type" : "Video",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Video 1.1",
				"title" : null,
				"value" : 21,
				"group" : 1,
				"pathId" : 0,
				"type" : "Video",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Url 1.8",
				"title" : null,
				"value" : 14,
				"group" : 1,
				"pathId" : 0,
				"type" : "Url",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Collaborative Wiki 1.3",
				"title" : null,
				"value" : 28,
				"group" : 1,
				"pathId" : 0,
				"type" : "Wiki",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Url 1.5",
				"title" : null,
				"value" : 10,
				"group" : 1,
				"pathId" : 0,
				"type" : "Url",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "Assessment Feedback 1.2",
				"title" : null,
				"value" : 26,
				"group" : 1,
				"pathId" : 0,
				"type" : "Feedback",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Document 1.9",
				"title" : null,
				"value" : 15,
				"group" : 1,
				"pathId" : 0,
				"type" : "Document",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Document 1.3",
				"title" : null,
				"value" : 19,
				"group" : 1,
				"pathId" : 0,
				"type" : "Document",
				"totalRequests" : 0,
				"totalUsers" : 0
			}, {
				"name" : "LearningObj Url 1.2",
				"title" : null,
				"value" : 17,
				"group" : 1,
				"pathId" : 0,
				"type" : "Url",
				"totalRequests" : 0,
				"totalUsers" : 0
			} ],
			"type" : "ResultListUserPathGraph"
		};
		d3custom.nodes = q2result.nodes;
		d3custom.links = q2result.links;
	})(window.d3custom = window.d3custom || {}, jQuery);
</script>


</@layout.page>
