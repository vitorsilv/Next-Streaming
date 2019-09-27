'use strict'

/** @typedef {import('@adonisjs/framework/src/Request')} Request */
/** @typedef {import('@adonisjs/framework/src/Response')} Response */
/** @typedef {import('@adonisjs/framework/src/View')} View */

/**
 * Resourceful controller for interacting with streamers
 */
const Streamer = use('App/Models/Streamer')
class StreamerController {
  /**
   * Show a list of all streamers.
   * GET streamers
   *
   * @param {object} ctx
   * @param {Request} ctx.request
   * @param {Response} ctx.response
   * @param {View} ctx.view
   */
  async index ({ request, response, view }) {
    try{
      const retornoSQL = await Streamer.all();
      let response = {
        data: retornoSQL,
        result:true,
        message:"Operação OK"
      }
      return response;
    }catch(err){
      let response = {
        data: null,
        result:false,
        message:err.message
      }
      return response;
    }
  }

  /**
   * Create/save a new streamer.
   * POST streamers
   *
   * @param {object} ctx
   * @param {Request} ctx.request
   * @param {Response} ctx.response
   */
  async store ({ request, response }) {
    let { nome,cpf,telefone,email,senha,rua,numero,complemento,estado,cidade,cep } = request.body;
    const streamer = new Streamer()
      streamer.nome = nome
      streamer.cpf = cpf
      streamer.telefone = telefone
      streamer.email = email
      streamer.senha = senha
      streamer.rua = rua
      streamer.numero = numero
      streamer.complemento = complemento
      streamer.estado = estado
      streamer.cidade = cidade
      streamer.cep = cep

      try{
        await streamer.save()

        let response = {
          data: streamer,
          result:true,
          message:"Cadastrado com sucesso"
        }

        return response;
      }catch(err){
        let response = {
          data: null,
          result:false,
          message:err.message
        }
        return response;
      }
  }

  /**
   * Display a single streamer.
   * GET streamers/:id
   *
   * @param {object} ctx
   * @param {Request} ctx.request
   * @param {Response} ctx.response
   * @param {View} ctx.view
   */
  async show ({ params, request, response, view }) {
    try{
      const retornoSQL = await Streamer.find(params.id);
      if(retornoSQL===null){
        let response = {
          data: retornoSQL,
          result:false,
          message:"Usuário não existe"
        }

        return response;
      }else{
        let response = {
          data: retornoSQL,
          result:true,
          message:"Operação OK"
        }

        return response;
      }
    }catch(err){
      let response = {
        data: null,
        result:false,
        message:err.message
      }

      return response;
    }
  }

  /**
   * Update streamer details.
   * PUT or PATCH streamers/:id
   *
   * @param {object} ctx
   * @param {Request} ctx.request
   * @param {Response} ctx.response
   */
  async update ({ params, request, response }) {
  }

  /**
   * Delete a streamer with id.
   * DELETE streamers/:id
   *
   * @param {object} ctx
   * @param {Request} ctx.request
   * @param {Response} ctx.response
   */
  async destroy ({ params, request, response }) {
  }
}

module.exports = StreamerController
