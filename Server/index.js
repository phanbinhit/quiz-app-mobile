const express = require('express');
const app = express();
const server = require('http').createServer(app);
const io = require('socket.io')(server);
const mongoose = require('mongoose');
const Exam = require('./models/exam.models');
const User = require('./models/user.models');

const PORT = 5000 || process.env.PORT;

app.set('view engine', 'pug');
app.set('views', './views');

app.get('/', (req, res) => {
    res.send('hello');
});
//connect mongodb
mongoose.connect('mongodb://localhost/quiz-db', {useNewUrlParser: true, useUnifiedTopology: true})
    .then( () => {
        console.log('Connected mongodb');
    })
    .catch( (err) => {
        console.log(err);
    });

io.sockets.on('connection', (socket) => {
    console.log('client connected');

    socket.on('client-send-result', async (data) => {
        let idUser = data.idStudent;
        let score = parseFloat(data.score);
        let roomId = data.roomId;
        let time = data.time;
        console.log(time);
        User.updateOne(
            {"idUser": idUser, "exams.roomId": roomId},
            {"$set": {"exams.$.isCompleted": true, "exams.$.score": score, "exams.$.time": time}},
            (err, docs) => {
                if (err) throw err;
            }
        );
    });

    socket.on('client-send-id-student', async (data) => {
    	let user = await User.findOne({"idUser": data.idStudent});
    	socket.emit('server-responsive-user', {user: user});
    })

    socket.on('client-send-login', async (data) => {
        let user = await User.findOne({idUser: data.id}, (err, docs) => {
            if (err) throw err;
        });
      
        if (!user) {
            socket.emit('server-send-login', {hasAccount: false});
        } else {
            if (data.password !== user.password) {
                socket.emit('server-send-login', {hasAccount: true, isRightPass: false});
            } else {
                socket.emit('server-send-login', {hasAccount: true, isRightPass: true, user: user});
            }
        }
    });

    socket.on('id-not-complete', async (data) => {
    	let ids = data.ids;
    	let idStudent = data.idStudent;
        let exams = [];
        for (let id of ids) {
            let exam = await Exam.findOne( {roomId: id} );
            exams.push(exam);
        }
        let user = await User.findOne({"idUser": idStudent});
        let examResults = [];
        for (let examResult of user.exams) {
        	for (let id of ids) {
        		if (examResult.roomId === id) {
        			examResults.push(examResult);
        			break;
        		}
        	}
        }
        
        socket.emit('exam-not-complete', {exams: exams, examResults: examResults});
    });

    socket.on('admin-request-exam', async (data) => {
        let exams = await Exam.find();
        socket.emit('server-send-exam', {exams: exams});
    });

    socket.on('admin-request-delete', async (data) => {
        // Exam.deleteOne({roomId: data}, (err, docs) => {
        //     if (err) throw err;
        // });
        let idUsers = [];
        let users = await User.find();
        for (let user of users) {
            if (user.role === "student") {
                for (let exam of user.exams) {
                    if (data === exam.roomId) {
                        idUsers.push(user.idUser);
                    }
                }
            }
        };
        for (let idUser of idUsers) {
            User.findOneAndDelete()
        }
        for (let idUser of idUsers) {
            User.updateOne(
                {"idUser": idUser},
                {$pull: {"exams": {"roomId": data}}},
                {multi: true}
            );
        }
    });

    socket.on('disconnect', () => {
        console.log('disconnect');
    });

});

function isContantUser(users, username) {
    for (let i = 0; i < users.length; i++) {
        if (users[i].user === username) {
            return true;
        }
    }
    return false;
}

server.listen(PORT, () => {
    console.log('Server is running in port ' + PORT);
})