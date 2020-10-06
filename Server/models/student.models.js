const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const StudentSchema = new Schema({
    idStudent: {type: String, required: true},
    password: {type: String, required: true},
    name: {type: String, required: true},
    className: {type: String, required: true},
    exams: {type: Array, required: true}
});
module.exports = mongoose.model('Student', StudentSchema, 'student')