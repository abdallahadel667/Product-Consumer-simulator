<template>
  <div class="contain">
    <div class="buttonsbar">
      <div class="machine">
        <button @click="addMachine">M+</button>
        <p>Add Machine</p>
      </div>
      <div class="queue">
        <button @click="addQueue">Q+</button>
        <p>Add Queue</p>
      </div>
      <div style="display: flex; flex-direction: row;">
        <p style="font-size: 25px;">No. of products: </p>
        <InputNumber style="width: 150px; height: 40px; position: relative; margin-top: 18px; margin-left: 5px;" v-model="number" inputId="minmax" :min="1" :max="200" />
      </div>
      <div>
        <button @click="connect" style="width: 120px;">Connect</button>
      </div>
      <button @click="clear" class="pi pi-trash"></button>

      <div class="sim-controls">
        <button @click="simulate" style="margin-right: 10px;" class="pi pi-play"></button>
        <!-- <button style="margin-right: 10px;" class="pi pi-stop"></button> -->
        <button @click="replay" style="margin-right: 10px;" class="pi pi-replay"></button>
      </div>
    </div>
  </div>
  <div class="canvas">
       <div ref="container" class="display-screen"></div>     
    </div>
</template>

<script>
import Konva from 'konva'
import InputNumber from 'primevue/inputnumber';
import axios from 'axios'
import {Stomp} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Stage } from 'konva/lib/Stage';

export default {
  components:{
    InputNumber,
  },

data(){

  return{
    tr: new Konva.Transformer({
      borderStrokeWidth: 7, // Set the border width
      }),
    queues:[],
    machines:[],  
    selected:[],
    adjacency:[],
    linelist:[],
    number:5,
    url:'http://localhost:8081/live',
    mode:'live',
    fetchData1Interval: null,
    fetchData2Interval: null,

  };
},

// created(){
//   const socket = new SockJS('http://localhost:8080/websocket-endpoint');
    
//     this.stompClient = Stomp.over(socket);
//     this.stompClient.connect({}, () => {
//       this.stompClient.subscribe('/topic/updates', (update) => {
//         console.log("here in update ")
//         console.log(update)
//         this.counterFromSpring = JSON.parse(update.body)
//       });
//     });



// },


mounted() {
     
     const screenWidth = 2940;
     const screenHeight = 850;
 
     this.stage = new Konva.Stage({
       container: this.$refs.container,
       width: screenWidth,
       height: screenHeight,
     });
 
     this.stage.on('dragend', (event) => {
       const shape = event.target;
     });
     this.layer = new Konva.Layer();
     this.stage.add(this.layer);
     this.layer.add(this.tr);
     this.tr.enabledAnchors([]);
     this.tr.rotateEnabled(false); // Disable rotation
    this.tr.anchorCornerRadius(0);


    // Set up an interval to call the fetchData function every 100 milliseconds
    // this.intervalId = setInterval(this.fetchData, 3000);
     
     
     
   }, 
   methods:{

    clear()
    {
      //write here the api responsible for clearing everything in the backend 


      this.adjacency=[];
      console.table(this.adjacency)
      for(let i =0;i<this.queues.length;i++)
       {
          this.queues[i].destroy();
       }
      this.queues=[];

      for(let i =0;i<this.machines.length;i++)
       {
          this.machines[i].destroy();
       }
      this.machines=[];
      for(let i =0;i<this.linelist.length;i++)
       {
          this.linelist[i].destroy();
       }
       this.linelist=[];
      this.selected=[];
      this.tr.nodes([]);
      

    },


//     fetchData() {
//       const base = this.url+this.mode;
//       console.log(base);
//       axios.get(base).then(response => {
  
//   console.log(response.data);  
//   let todisplay = response.data;
//   for(const iterator in todisplay)
//   {
//     if (todisplay[iterator].id.startsWith('Q')) {
//     // For 'Q' items
//     const queueId = todisplay[iterator].id;  // "Q0", "Q1", ...
//     const numericPart = queueId.slice(1);
//     const numberr = parseInt(numericPart, 10);
//     const numberOfElements = todisplay[iterator].numberOfElements;
//     this.queues[numberr].children[2].text(numberOfElements)

//     // Use the data for queues
//     // console.log(`Queue ID: ${queueId}, Number of Elements: ${numberOfElements}`);
//   } else if (todisplay[iterator].id.startsWith('M')) {
//     // For 'M' items
//     const machineId = todisplay[iterator].id;  // "M1", "M2", ...
//     const numericPart = machineId.slice(1);
//     const numberr = parseInt(numericPart, 10);
//     // const numberOfElements = todisplay[iterator].numberOfElements;
//     const colorr = todisplay[iterator].colour;
//     const red = parseInt(colorr[0], 10);
//     const green = parseInt(colorr[1], 10);
//     const blue = parseInt(colorr[2], 10);

//     // Create an RGB color string
//     const color = `rgb(${red}, ${green}, ${blue})`;
//     this.machines[numberr].children[0].fill('#008000');

//     this.machines[numberr].children[0].fill(color);

    

//     // Use the data for machines
//     // console.log(`Machine ID: ${machineId}, Number of Elements: ${numberOfElements}`);
//   }


//   }   
  
//   // Assume 'jsonResponse' is the JSON response you received

// // Filter and process the data


//     })
//     .catch(error => {
//       console.error('There was an error!', error);
      
//     })
//       // console.log('Fetching data...');
//     },


    startFetchData1() {
      // Clear the interval for fetchData2 if it's running
      clearInterval(this.fetchData2Interval);

      // Set up an interval for fetchData1
      this.fetchData1Interval = setInterval(this.fetchData1, 3000);
    },
    startFetchData2() {
      // Clear the interval for fetchData1 if it's running
      clearInterval(this.fetchData1Interval);

      // Set up an interval for fetchData2
      this.fetchData2Interval = setInterval(this.fetchData2, 3000);
    },

    fetchData1() {
      // console.log('Fetching data 1...');
      // Your fetchData1 logic here
      axios.get('http://localhost:8081/live').then(response => {
  
  console.log(response.data);  
  let todisplay = response.data;
  for(const iterator in todisplay)
  {
    if (todisplay[iterator].id.startsWith('Q')) {
    // For 'Q' items
    const queueId = todisplay[iterator].id;  // "Q0", "Q1", ...
    const numericPart = queueId.slice(1);
    const numberr = parseInt(numericPart, 10);
    const numberOfElements = todisplay[iterator].numberOfElements;
    this.queues[numberr].children[2].text(numberOfElements)

    // Use the data for queues
    // console.log(`Queue ID: ${queueId}, Number of Elements: ${numberOfElements}`);
  } else if (todisplay[iterator].id.startsWith('M')) {
    // For 'M' items
    const machineId = todisplay[iterator].id;  // "M1", "M2", ...
    const numericPart = machineId.slice(1);
    const numberr = parseInt(numericPart, 10);
    // const numberOfElements = todisplay[iterator].numberOfElements;
    const colorr = todisplay[iterator].colour;
    const red = parseInt(colorr[0], 10);
    const green = parseInt(colorr[1], 10);
    const blue = parseInt(colorr[2], 10);

    // Create an RGB color string
    const color = `rgb(${red}, ${green}, ${blue})`;
    this.machines[numberr].children[0].fill('#008000');

    this.machines[numberr].children[0].fill(color);

    

    // Use the data for machines
    // console.log(`Machine ID: ${machineId}, Number of Elements: ${numberOfElements}`);
  }


  }   
  
  // Assume 'jsonResponse' is the JSON response you received

// Filter and process the data


    })
    .catch(error => {
      console.error('There was an error!', error);
      
    })
      // console.log('Fetching data...');
    },



    

    simulate(){
      console.table(this.adjacency);
      const body={architecture:this.adjacency}
      axios.post(`http://localhost:8081/construct/${this.number}`,body);
      this.startFetchData1();


//
    },





    fetchData2() {
      //  console.log('Fetching data 2...');
      // Your fetchData1 logic here
      axios.get('http://localhost:8081/live').then(response => {
  
  console.log(response.data);  
  let todisplay = response.data;
  for(const iterator in todisplay)
  {
    if (todisplay[iterator].id.startsWith('Q')) {
    // For 'Q' items
    const queueId = todisplay[iterator].id;  // "Q0", "Q1", ...
    const numericPart = queueId.slice(1);
    const numberr = parseInt(numericPart, 10);
    const numberOfElements = todisplay[iterator].numberOfElements;
    if(numberr===this.queues.length-1)
    this.queues[numberr].children[2].text(numberOfElements-this.number)
    else
    this.queues[numberr].children[2].text(numberOfElements)

    // Use the data for queues
    // console.log(`Queue ID: ${queueId}, Number of Elements: ${numberOfElements}`);
  } else if (todisplay[iterator].id.startsWith('M')) {
    // For 'M' items
    const machineId = todisplay[iterator].id;  // "M1", "M2", ...
    const numericPart = machineId.slice(1);
    const numberr = parseInt(numericPart, 10);
    // const numberOfElements = todisplay[iterator].numberOfElements;
    const colorr = todisplay[iterator].colour;
    const red = parseInt(colorr[0], 10);
    const green = parseInt(colorr[1], 10);
    const blue = parseInt(colorr[2], 10);

    // Create an RGB color string
    const color = `rgb(${red}, ${green}, ${blue})`;
    this.machines[numberr].children[0].fill('#008000');

    this.machines[numberr].children[0].fill(color);

    

    // Use the data for machines
    // console.log(`Machine ID: ${machineId}, Number of Elements: ${numberOfElements}`);
  }


  }   
  
  // Assume 'jsonResponse' is the JSON response you received

// Filter and process the data


    })
    .catch(error => {
      console.error('There was an error!', error);
      
    })
      // console.log('Fetching data...');
    },




    replay(){
      axios.get('http://localhost:8081/replay');
      this.startFetchData2();
    },

    connect(){
    //  console.log(this.tr.nodes().length)
   
     if(this.tr.nodes().length==2){

      
     
     if(this.tr.nodes()[0].children[0] instanceof Konva.Circle && this.tr.nodes()[1].children[0] instanceof Konva.Rect)
     {
      // console.log('lolo')
      // console.log(this.tr.nodes()[1].x());

      const line = new Konva.Line({
          points: [this.tr.nodes()[1].x()+100,this.tr.nodes()[1].y()+50,
           this.tr.nodes()[0].x(), this.tr.nodes()[0].y()],
          stroke: 'green',
          strokeWidth: 4,
         
        });
        // console.log('55555')
        // console.log(this.tr.nodes()[0].children[0])


        // Add the line to the layer
        this.layer.add(line);
        this.linelist.push(line);

        // Redraw the layer
        this.layer.draw();
        this.adjacency[this.tr.nodes()[0].children[0].id()][this.tr.nodes()[1].children[0].id()]=1;
        console.table(this.adjacency)
        
     }

     else if(this.tr.nodes()[1].children[0] instanceof Konva.Circle && this.tr.nodes()[0].children[0] instanceof Konva.Rect) {


      const line = new Konva.Line({
          points: [this.tr.nodes()[0].x()+100,this.tr.nodes()[0].y()+50,
           this.tr.nodes()[1].x(), this.tr.nodes()[1].y()],
          stroke: 'yellow',
          strokeWidth: 4,
         
        });
        
        this.layer.add(line);
        this.linelist.push(line);

        // Redraw the layer
        this.layer.draw();
        this.adjacency[this.tr.nodes()[1].children[0].id()][this.tr.nodes()[0].children[0].id()]=-1;
        console.table(this.adjacency)


     }
    }
    else{}
      
    },

    addDoubleClickEvent(shape) {
   shape.on('click', () => {
     shape.moveToTop();
     if(this.selected.length<2)
     {
      this.selected.push(shape);
      this.tr.nodes(this.selected);
     }
     else{
      this.selected=[];
      this.selected.push(shape);
      this.tr.nodes(this.selected);

     }
     
     this.stage.batchDraw();
   });
 
   this.stage.on('click', (e) => {
     if (e.target === this.stage) {
       if (this.tr.nodes().length > 0) {
         // Commented out to prevent calling onModify when clicking outside the shape
         // this.onModify(this.tr.nodes()[0]);
       }
        this.tr.nodes([]);
        this.selected=[];
       return;
     }
   });},
   addQueue(){
      var rect = new Konva.Group({
        x: 50, 
        y: 50, 
        width: 200,
        height: 100,
        // rotation: angle, 
        draggable: true,
    }); 

    rect.add(new Konva.Rect({
      width: 200,
      height: 100,
      fill:'#FFFF00',
      draggable: true,
      id:this.queues.length,
    }));

    rect.add(new Konva.Text({
        text:"Q"+this.queues.length,
        fontSize: 30,
        fontFamily: 'Calibri',
        fill: '#000',
        width: 200,
        height:100,
        padding: 5,
        align: 'center',
        verticalAlign: 'middle', 

    }));

    rect.add(new Konva.Text({
    text: "0",
    fontSize: 25,
    fontFamily: 'Calibri',
    fill: '#000',
    width: 200,
    height: 100,
    padding: 5,
    align: 'center',
    verticalAlign: 'bottom', 
    }));

       this.addDoubleClickEvent(rect);
       this.layer.add(rect);
       this.queues.push(rect);
       console.log(this.queues)
         //  console.log(this.queues[0].children[1].text());
       this.layer.draw();
       this.initialize();

   },
   addMachine(){
       var circle = new Konva.Group({
          x: 150,
          y: 150,
          draggable: true,
      });

      circle.add(new Konva.Circle({
          radius: 75,
          fill: '#008000',
          draggable: true,
          id:this.machines.length,
      }));

      circle.add(new Konva.Text({
          text:"M"+this.machines.length,
          fontSize: 50,
          fontFamily: 'Calibri',
          fill: 'black',
          width: 100,
          padding:5,
          height:100,
          align: 'center',
          verticalAlign: 'middle',
          offsetX:50,
          offsetY:45,
         
      }));
            

       this.addDoubleClickEvent(circle);
       this.layer.add(circle);
       this.machines.push(circle);
       this.layer.draw();
       this.initialize();

       

   },

   initialize()
   {
    this.adjacency=[]
    for (var i = 0; i < this.machines.length; i++) {
        var row = [];
        for (var j = 0; j < this.queues.length; j++) {
            row.push(0); // Initialize each element to 0
        }
        this.adjacency.push(row);
    }

   }


   },

}
</script>

<style>
p{
  font-family: Helvetica, Arial, sans-serif;
  /* font-size: small; */
}

Button {
  /* margin: 10px 5px 1px 10px;
  margin-left: 1px;
  margin-right: 1px;
  margin-top: 1px;
  margin-bottom: 1px; */
  height: 75px;
  width: 75px;
  border-radius: 20%;
  font-size: 25px;
  background-color: rgb(208, 207, 207);
}
Button:hover{
  background-color:rgb(137, 135, 135);;
}

.sim-controls {
  border-radius: 50%;
  display: flex;
  /* justify-content: space-between; */
}

.buttonsbar {
  display: flex;
  justify-content: space-around;
  font-size: 20px;
}

.machine {
  justify-content: center;
  display: block;
  
}
.display-screen {
   border: 2px solid #000000;
   background-color: white;
   position: absolute;
   top: 25%;
   width: 99%;
 }
 .contain{
  height: 100%;
 }
</style>