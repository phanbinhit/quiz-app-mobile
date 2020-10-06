const express = require('express');
const app = express();
const server = require('http').createServer(app);
const io = require('socket.io')(server);
const mongoose = require('mongoose');
const Exam = require('./models/exam.models');
const Student = require('./models/student.models');

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
    socket.on('client-send-roomId', (roomId) => {
        Exam.findOne( {roomId: roomId} )
            .then((exam) => {
                if (!exam) {
                    socket.emit('server-send-roomId', {exist: false});
                } else {
                    socket.emit('server-send-roomId', {exist: true});
                }
            })
            .catch((err) => {
                console.log(err);
            })
    });

    socket.on('client-request-exam', async (data) => {
        let exam = await Exam.findOne( {roomId: data} );
        socket.emit('server-send-exam', {exam: exam});
    });

    socket.on('client-send-result', (data) => {
        let idStudent = data.idStudent;
        let score = parseFloat(data.score);
        let roomId = data.roomId;

        Student.updateOne(
            {"idStudent": idStudent, "exams.roomId": roomId},
            {$set: {"exams.$.score": score, "exams.$.isCompleted": true}},
            (err, docs) => {
                if (err) throw err;
            }
        );
    });

    socket.on('client-sent-user', async (data) => {
        let exam = await Exam.findOne( {roomId: data.roomId} );
        let username = data.username;
        let results = exam.results;
        if (isContantUser(results, username)) {
            socket.emit('is-contant-user', {contant: true});
        } else {
            socket.emit('is-contant-user', {contant: false});
        }
    });

    socket.on('client-send-login', async (data) => {
        let student = await Student.findOne({idStudent: data.id}, (err, docs) => {
            if (err) throw err;
        });
      
        if (!student) {
            socket.emit('server-send-login', {hasAccount: false});
        } else {
            if (data.password !== student.password) {
                socket.emit('server-send-login', {hasAccount: true, isRightPass: false});
            } else {
                socket.emit('server-send-login', {hasAccount: true, isRightPass: true, student: student});
            }
        }
    });

    socket.on('id-not-complete', async (data) => {
        let exams = [];
        for (let id of data) {
            let exam = await Exam.findOne( {roomId: id} );
            exams.push(exam);
        }
        socket.emit('exam-not-complete', {exams: exams});
    })

    socket.on('disconnect', () => {
        console.log('disconnect');
    })
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