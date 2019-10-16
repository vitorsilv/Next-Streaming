'use strict'

/** @typedef {import('@adonisjs/framework/src/Request')} Request */
/** @typedef {import('@adonisjs/framework/src/Response')} Response */
/** @typedef {import('@adonisjs/framework/src/View')} View */

/**
 * Resourceful controller for interacting with streamers
 */
const Streamer = use('App/Models/Streamer')
const Endereco = use('App/Models/Endereco')
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
      const retornoSQL = await Streamer.query().with('endereco').fetch();
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
    let { nome,
          cpf,
          telefone,
          email,
          senha,
          confsenha,
          rua,
          numero,
          complemento,
          uf,
          bairro,
          cidade,
          cep
        } = request.body;
    if(senha!=confsenha){
      let response = {
        data: null,
        result:false,
        message:"Senhas não conferem"
      }

      return response;
    }
    const streamer = new Streamer()
      streamer.nome = nome
      streamer.cpf = cpf
      streamer.telefone = telefone
      streamer.email = email
      streamer.senha = senha

    const endereco = new Endereco();
      endereco.logradouro = rua
      endereco.numero = numero
      endereco.complemento = complemento
      endereco.bairro = bairro
      endereco.cidade = cidade
      endereco.uf = uf
      endereco.cep = cep

      try{
        await streamer.endereco().save(endereco)

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
      const retornoSQL = await Streamer.query()
        .where('idStreamer',params.id)
        .with('endereco')
      .fetch();

      if(retornoSQL===null){
        let response = {
          data: null,
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
    try{
      const streamer = await Streamer.find(params.id)
      if(streamer===null){
        let response = {
          data: null,
          result:false,
          message:"Usuário não existe"
        }

        return response;
      }else{
      await streamer.delete()
      let response = {
        data: streamer,
        result:true,
        message:"Usuário deletado com sucesso!"
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

  async login ({ params, request, response, view }) {

    const { email , senha } = request.body;
    try{

      const retornoSQL = await Streamer.findBy('email',email);
      if(retornoSQL===null){
        let response = {
          data: null,
          result:false,
          message:"Usuário não existe"
        }

        return response;
      }else{
        if(email===retornoSQL.email && senha===retornoSQL.senha){
          let response = {
            data: retornoSQL,
            result:true,
            message:"Usuário logado"
          }

          return response;
        }else{
          let response = {
            data: null,
            result:false,
            message:"Email e/ou senha incorretos"
          }

          return response;
        }
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
}

module.exports = StreamerController
