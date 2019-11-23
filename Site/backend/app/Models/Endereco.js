'use strict'

/** @type {typeof import('@adonisjs/lucid/src/Lucid/Model')} */
const Model = use('Model')

class Endereco extends Model {
  static get table(){
    return 'endereco';
  }
  static get incrementing () {
    return true
  }
  static get primaryKey(){
    return 'idEndereco';
  }
  static get createdAtColumn () {
    return null
  }
  static get updatedAtColumn () {
    return null
  }
}

module.exports = Endereco
