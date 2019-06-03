window.addEventListener('load', function(){

    //Vue.js element
    const vue = new Vue({
        el: "#app",
        data: function(){
            return{
                //counter number of offensive tweets
                offensive: 10,
                //counter number of offensive tweets
                nonOffensive: 10,
                //pie chart time selection value
                selectPieTime: '',
                //time selection values
                times: [
                    {text: "Letzter Tag", value: "day"}, 
                    {text: "Letzte Woche", value: "week"}, 
                    {text: "Letzter Monat", value: "month"}, 
                ],
                //datepicker selection, initialized with one week
                selectedDate: {
                    start:  getDate(-7),
                    end:  getDate(0)
                }, 
            }
        },
        methods:{
            //update data and display of charts
            updateCharts: function(){
                pieChart.data.datasets[0].data = [this.offensive, this.nonOffensive]
                pieChart.update()
                lineChart.data.datasets[0].data[lineChart.data.datasets[0].data.length-1] = [this.offensive]
                lineChart.data.datasets[1].data[lineChart.data.datasets[1].data.length-1] = [this.nonOffensive]
                lineChart.update()
            },
            //handle the counter inputs
            handleInput: function(){
                this.filterInput()
                this.updateCharts()
            },
            //filter the counter inputs
            filterInput: function(){
                if(this.offensive<0){
                    this.offensive=0
                }
                if(this.nonOffensive<0){
                    this.nonOffensive=0
                }
            },
            //Show info for when pie chart should be updated
            updatePieChartTime: function(){
                console.log("Backend-Request: get number of offensive and non-offensive Tweets for past " + this.selectPieTime)
            },

            //Update the line chart labels based on the selected start-/enddate
            //To-Do: Change the dataset aswell -> has to be requested from backend
            updateLineChart: function(){
                dateRange = getRangeOfDates(moment(this.selectedDate.start), moment(this.selectedDate.end), 'day')
                lineChart.data.labels=dateRange
                offData = getDataPoints("off", dateRange[0], dateRange[dateRange.length-1], dateRange.length)
                nonOffData = getDataPoints("nonOff", dateRange[0], dateRange[dateRange.length-1], dateRange.length)
                lineChart.data.datasets[0].data = offData
                lineChart.data.datasets[1].data = nonOffData
                lineChart.update()
            }
        
        }

    })

    //Used Chart.js as it seemed easier (compared to D3.js) to quickly implement the graphs we need (atleast for now)

    var ctx2 = document.getElementById('pieChart').getContext('2d');
    var pieChart = new Chart(ctx2, {
    // type of chart
    type: 'pie',

    //Data and style options for the line chart
    data: {
        labels: ['offensive', 'non-offensive'],
        datasets: [{
            backgroundColor: ['rgb(255, 99, 132)','rgb(0,255,0)'],
            borderColor: 'rgb(255,255,255)',
            fill: false,
            data: [vue.offensive, vue.nonOffensive]
        },
    ]
    },

    // Configuration options go here
    options: {
        rotation: 0.5 * Math.PI
    }
    });
    

    //Line chart - comparing offensive/nonOffensive over specified time
    var ctx2 = document.getElementById('lineChart').getContext('2d');
    var lineChart = new Chart(ctx2, {
    // type of chart
    type: 'line',

    //Data and style options for the line chart
    data: {
        labels: [''],
        datasets: [{
            label: 'offensive',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            fill: false,
            data: [3, 7, 10, 10]
        },
        {
            label: 'non-offensive',
            backgroundColor: 'rgb(0, 255, 0)',
            borderColor: 'rgb(0, 255, 0)',
            fill: false,
            data: [2, 5, 8, 10]
        },
    ]
    },

    // Configuration options go here
    options: {
        elements: {
            line: {
                tension: 0 // disables bezier curves
            }
        },
        scales: {
            yAxes: [{
                ticks:{
                    beginAtZero: true //y-Axis starts at 0
                }
            }]
        }
    }
    });

    //initialise Tweets and Line Chart
    function init(){
        displayTweet()
        vue.updateLineChart()
    }
    
   //run init function
    init()

});

//format a given date for easier display as chart label
function formatDate(date){
    return date.getDate() + "." + (date.getMonth()+1)  + "." + date.getFullYear().toString().slice(-2)
}

//Gets range of dates  between start and end date with key (for example 'day') steps 
function getRangeOfDates(start, end, key, arr = [start.startOf(key)]) {
  
    if(start.isAfter(end)) throw new Error('start must precede end')
    
    const next = moment(start).add(1, key).startOf(key);
    
    if(next.isAfter(end, key)) return arr.map((v) => formatDate(v.toDate()))
    
    return getRangeOfDates(next, end, key, arr.concat(next));
    
}

 //Get the tweets to be displayed
 function displayTweet(){

    //example for tweet ID of a possibly non offensive tweet
    nonOffTweetId = 507185938620219395

    //To-Do: Request html data from the backend, which retrieves the data from the Twitter-Api
    axios.get('https://cors-anywhere.herokuapp.com/https://publish.twitter.com/oembed?url=https%3A%2F%2Ftwitter.com%2Fx%2Fstatus%2F507185938620219395&align=center'
    )
        .then(function (response) {
        // handle success

        //set the corresponding div content to received html
        document.getElementById("nonOffTweet").innerHTML = response.data.html
        twttr.widgets.load()

    })
    .catch(function (error) {
        // handle error
        console.log(error)
    })
    .finally(function () {
        // always executed
    })

    //example for tweet ID of a possibly offensive tweet
    offTweetId = 1130427754971881472

    axios.get('https://cors-anywhere.herokuapp.com/https://publish.twitter.com/oembed?url=https%3A%2F%2Ftwitter.com%2Fx%2Fstatus%2F1130427754971881472&align=center'
    )
        .then(function (response) {
        // handle success

        document.getElementById("offTweet").innerHTML = response.data.html
        twttr.widgets.load()

    })
    .catch(function (error) {
        // handle error
        console.log(error)
    })
    .finally(function () {
        // always executed
    })
    
}

//get number of tweets for each day between start and end date for specified label, currently just returns dummy data
//To-Do: get real data from Backend
function getDataPoints(label, start, end, l){
    if(label === "off"){
        //To-Do: Axios.get() request number of offensive tweets for each day from start to end 

        //placeholder return
        return Array.from({length: l}, () => Math.floor(Math.random() * 25));
    }else if(label === "nonOff"){
        //To-Do: Axios.get() request number of non-offensive tweets for each day from start to end 

        //placeholder return
        return Array.from({length: l}, () => Math.floor(Math.random() * 25));
    }
}

 //get todays date +(future)/-(past) a number of days
function getDate(d){
    if(d > 0){
        return moment().add(d,'days').toDate()
    }else if(d < 0){
        return moment().subtract(Math.abs(d), 'days').toDate()
    }else if(d === 0){
        return moment().toDate()
    }
}