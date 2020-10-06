const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const ExamSchema = new Schema({
    title: {type: String, required: true},
    roomId: {type: String, required: true},
    questions: {type: Array, default: [], required: true},
    time: {type: Number, required: true}
});

module.exports = mongoose.model('Exam', ExamSchema, 'exams');