window.addEventListener('load', function(){

    displayTweet()
    //Vue.js element
    const vue = new Vue({
        el: "#app",
        data: function(){
            return{
                //counter number of offensive tweets
                offensive: 10,
                //counter number of offensive tweets
                nonOffensive: 10,
                //bar chart time selection value
                selectBarTime: '',
                //bar chart time selection value
                selectLineTime: '',
                //time selection values
                times: [
                    {text: "Letzter Tag", value: "day"}, 
                    {text: "Letzte Woche", value: "week"}, 
                    {text: "Letzter Monat", value: "month"}, 
                ]	
            }
        },
        methods:{
            //update data and display of charts
            updateCharts: function(){
                barChart.data.datasets[0].data = [vue.offensive, vue.nonOffensive, vue.offensive+vue.nonOffensive]
                barChart.update()
                lineChart.data.datasets[0].data[lineChart.data.datasets[0].data.length-1] = [vue.offensive]
                lineChart.data.datasets[1].data[lineChart.data.datasets[1].data.length-1] = [vue.nonOffensive]
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
            //Show info for when bar chart should be updated
            updateBarChartTime: function(){
                alert("barchart time update")
            },
            //Show info for when line chart should be updated
            updateLineChartTime: function(){
                alert("linechart time update")
            },

        }

    })

    //Used Chart.js as it seemed easier to quickly implement a prototype (compared to D3.js)

    //Bar chart - shows number of offensive/nonOffensive/sum tweets in specified time
    var ctx = document.getElementById('barChart').getContext('2d');
    var barChart = new Chart(ctx, {
    // The type of chart we want to create
    type: 'bar',

    // The data for our dataset
    data: {
        labels: ['Offensive', 'Non-Offensive', 'Gesamt'],
        datasets: [
            {
                label: 'Anzahl Tweets',
                backgroundColor: 'rgb(0, 0, 255)',
                borderColor: 'rgb(0,0,0)',
                borderWidth: '1',
                data: [vue.offensive, vue.nonOffensive, vue.offensive+vue.nonOffensive]

            }]
    },

    // Configuration options go here
    options: {
        scales: {
            yAxes: [{
                ticks:{
                    beginAtZero: true
                }
            }]
        }
    }
    });

    //Line chart - comparing offensive/nonOffensive over specified time
    var ctx2 = document.getElementById('lineChart').getContext('2d');
    var lineChart = new Chart(ctx2, {
    // The type of chart we want to create
    type: 'line',

    // The data for our dataset
    data: {
        labels: ['Januar', 'Februar', 'März', 'April'],
        datasets: [{
            label: 'Anzahl offensive Tweets',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            fill: false,
            data: [3, 7, 10, 10]
        },
        {
            label: 'Anzahl non-offensive Tweets',
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
                    beginAtZero: true
                }
            }]
        }
    }
    });
    
    function displayTweet(){

        nonOffTweetId = 507185938620219395

        axios.get('https://cors-anywhere.herokuapp.com/https://publish.twitter.com/oembed?url=https%3A%2F%2Ftwitter.com%2Fx%2Fstatus%2F507185938620219395&align=center'
        )
            .then(function (response) {
            // handle success

            console.log(response.data.html)

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

        offTweetId = 1130427754971881472

        axios.get('https://cors-anywhere.herokuapp.com/https://publish.twitter.com/oembed?url=https%3A%2F%2Ftwitter.com%2Fx%2Fstatus%2F1130427754971881472&align=center'
        )
            .then(function (response) {
            // handle success

            console.log(response.data.html)

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
})