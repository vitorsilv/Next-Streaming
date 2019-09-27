'use strict'

/** @type {typeof import('@adonisjs/lucid/src/Lucid/Model')} */
const Model = use('Model')

class Streamer extends Model {
  static get table(){
    return 'streamer';
  }
  static get incrementing () {
    return true
  }
  static get primaryKey(){
    return 'idStreamer';
  }
}

module.exports = Streamer
