let a,b;
let aime = 1,bime = 1;


const express = require('express');
const app = express();
const http = require('http');
const socket = require('socket.io');
const server = http.createServer(app);
const io = socket(server);
app.get('/', (req, res) => {!
  res.send('<h1>Hello world</h1>');
});

server.listen(4001,'localhost', () => {
  console.log('listening on *:3001');
});


io.on('connection', (socket) => {
    console.log("New socket connection: " + socket.id);
    if(a == null )
    {
        a = socket.id;
        io.to(socket.id).emit("pleyer1",true);
       // console.log("peyer1");
    }
    else{
      if(b == null){
        b = socket.id;
        io.to(socket.id).emit("pleyer2",true);
     //   console.log("peyer2");
       
      }
    }
    socket.on('start', () => {
      io.emit("startMatch",true);
     
    
  })
    socket.on('Imena', () => {
      const person = {a:aime, b:bime};
      io.emit("podaci",JSON.stringify(person)); 
     
    
  })
   socket.on('turn', async () => {

       if (socket.id == a) {
           io.to(b).emit("changeturn");
       }
       if (socket.id == b) {
           io.to(a).emit("changeturn");
       }


   })
    socket.on('turna', async () => {
        await new Promise(resolve => setTimeout(resolve, 1000));
        if (socket.id == a) {
            io.to(b).emit("changeturna");
        }
        if (socket.id == b) {
            io.to(a).emit("changeturna");
        }


    })

    socket.on('open', (ab) => {

        if(socket.id == a)
        {
            io.to(b).emit("opens",ab);
        }
        if(socket.id == b)
        {
            io.to(a).emit("opens",ab);
        }


    })

    socket.on('needed', (ab) => {

        if(socket.id == a)
        {
            io.to(b).emit("neededc",ab);
        }
        if(socket.id == b)
        {
            io.to(a).emit("neededc",ab);
        }


    })

    socket.on('finalNumberGame', () => {


            io.emit("finalNumberGamea");



    })
    socket.on('myguess', (ab) => {

        if(socket.id == a)
        {
            io.to(b).emit("myguessc",ab);
        }
        if(socket.id == b)
        {
            io.to(a).emit("myguessc",ab);
        }


    })
    socket.on('openColumn', (ab) => {

        if(socket.id == a)
        {
            io.to(b).emit("opencs",ab);
        }
        if(socket.id == b)
        {
            io.to(a).emit("opencs",ab);
        }


    })

    socket.on('Pobeda', (ab) => {

        if(socket.id == a)
        {
         //  console.log("a");
            io.to(b).emit("ennemywin");
        }
        if(socket.id == b)
        {
            io.to(a).emit("ennemywin");
        }


    })
    socket.on('enemyguess', (av,bv,cv,dv) => {

        if(socket.id == a)
        {

            const person = {a:av, b:bv,c:cv,d:dv};
            console.log(person.toString());
            io.to(b).emit("enemyguessc",person);
        }
        if(socket.id == b)
        {
            const person = {a:av, b:bv,c:cv,d:dv};
            console.log(person.toString());
            io.to(a).emit("enemyguessc",person);
        }


    })
    socket.on('pointsca', (ab) => {

        if(socket.id == a)
        {
            ////////////  console.log("a");
            io.to(b).emit("pointscac",ab);
        }
        if(socket.id == b)
        {
            io.to(a).emit("pointscac",ab);
        }


    })
    socket.on('points', (ab) => {

        if(socket.id == a)
        {
        ////////////  console.log("a");
            io.to(b).emit("pointsc");
        }
        if(socket.id == b)
        {
            io.to(a).emit("pointsc");
        }


    })
    socket.on('nextgamec', (ab) => {


        if (socket.id == a) {
            /// console.log("a");
            io.to(b).emit("nextgamecc");
        }
        if (socket.id == b) {
            io.to(a).emit("nextgamecc");
        }


    })
    socket.on('lopta', (av,bv) => {
        const person = {a:av, b:bv};
        if(socket.id == a)
        {
//s
            io.to(b).emit("loptac",person);
        }
        if(socket.id == b)
        {
            io.to(a).emit("loptac",person);
        }


    })
    socket.on('guess', (av,bv,cv,dv) => {
        const person = {a:av, b:bv,c:cv,d:dv};
        if(socket.id == a)
        {

            io.to(b).emit("guessc",person);
        }
        if(socket.id == b)
        {
            io.to(a).emit("guessc",person);
        }


    })
    socket.on('nextstep', () => {


        if (socket.id == a) {

            io.to(b).emit("nextstepc");
        }
        if (socket.id == b) {
            io.to(a).emit("nextstepc");
        }


    })
    socket.on('passcom', async (av, bv, cv, dv) => {
        const person = {a: av, b: bv, c: cv, d: dv};

        if (socket.id == a) {

            io.to(b).emit("passcomc", person);
        }
        if (socket.id == b) {
            io.to(a).emit("passcomc", person);
        }


    })
    socket.on('Ime', (zika) => {
       
      if(a != null && aime == 1)
    {
      aime = zika;
    }
    else{
      if(b != null){
       bime = zika;
      }
    }
  })
    socket.on('reset', () => {
       
        a= null;
        b= null;
    })
})