(function(d3custom, $) {

  d3custom.run = function() {
	  	var cal1 = new CalHeatMap();
	  	var cal2 = new CalHeatMap();
	  	var transformedData = transformData(d3custom.data);
	  	var minVal = 100000000;
	  	var maxVal = 0;
	  	var timeProgress = calcStartDate(d3custom.data);
	  	var startDate = timeProgress.startDate;
	  	var endDate = timeProgress.endDate;

	  	
	  	function calcStartDate(data){
	  		var tmin = new Date().getTime();
	  		var tmax = 0;
	  		for(var timestamp in data){
	  			if(timestamp < tmin){
	  				tmin = timestamp; 
	  			}
	  			if(timestamp > tmax){
	  				tmax = timestamp; 
	  			}	  			
	  			if(data[timestamp]>maxVal)
	  				maxVal = data[timestamp];
	  			if(data[timestamp]<minVal)
	  				minVal = data[timestamp];
	  		}
	  		return {startDate:new Date(tmin*1000),endDate:new Date(tmax*1000)};
	  	}
	  	
	  	function transformData(data){
	  		return data;
	  	}
	  	
	  	function monthDiff(d1, d2) {
	  	    var months;
	  	    months = (d2.getFullYear() - d1.getFullYear()) * 12;
	  	    months -= d1.getMonth();
	  	    months += d2.getMonth()+1;
	  	    return months <= 0 ? 0 : months;
	  	}
	  	
	  	var numberOfMonthToDisplay=monthDiff(startDate,endDate);
	  	var vectorSize = 100;
	  	var vector = new Array(vectorSize);
	  	var colorScale = d3.scale.pow().exponent(2);
	  	colorScale.range([minVal,maxVal]);
	  	colorScale.domain([0,vectorSize-1]);
	  	var minStartVal = minVal;
	  	for(var j=0; j<vectorSize; j+=1){
	  	    vector[j]=1+colorScale(j);
	  	}
	  	for (var i = 0; i < numberOfMonthToDisplay; i++){
	  		var heatmapDiv = "cal-heatmap"+(i+1).toString();
	    	$("#viz").append('<div class="maps" id="'+heatmapDiv+'"></div>');	    	     	  		
	  		var cal = new CalHeatMap();
	  		cal.init({
		    	itemSelector: "#"+heatmapDiv,
		        data: transformedData,
		        start: new Date(startDate.getFullYear(), startDate.getMonth()+i, 1),
		        range: 1, 
		        domain: "month", 
		        subDomain: "day",
		        domainMargin: 4,
		        cellSize: 25,
				subDomainTextFormat: "%d",
		        browsing: true,
		        legendColors: {
		    		min: "#efefef",
		    		max: "#ff0000",
		    		empty: "white"
		    	},
		    	legend : vector, //[0,5,10,20,90,200],
		        displayLegend: false,   
		        considerMissingDataAsZero: true,
			})
	  	}
  };

})(window.d3custom = window.d3custom || {}, jQuery);

$(document).ready(window.d3custom.run);
