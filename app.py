'''
Instituto Federal de Santa Catarina - Campus São José
Engenharia de Telecomunicações
Projeto 2 Banco de Dados - Urna eletrônica WEB
Aluna: Maria Fernanda Tutui
'''
from _curses import flash
from flask import render_template, url_for, session, redirect, request
from flask_nav.elements import Navbar, View, Subgroup
from meusforms import *
from models import *
from datetime import datetime
from flask import Flask, render_template
from flask_bootstrap import Bootstrap
from flask_nav import Nav
from flask_nav.elements import Navbar, View, Subgroup, Link
from collections import Counter

@nav.navigation()
def meunavbar():
    '''
    Padronização da barra de navegação da tela inicial.
    :return:
    '''
    menu = Navbar('BCD29008 - Banco de dados')
    menu.items = [View('Login', 'autenticar')]
    return menu

@nav.navigation()
def meunavbar_autenticado_admin():
    '''
    Padronização da barra de navegação para as páginas do usuário administrador.
    :return:
    '''
    menu = Navbar('BCD29008 - Banco de dados')
    menu.items = [View('Home','autenticado_admin'), Subgroup('Eleição',View('Criar Eleição','criar_eleicao'),View('Adicionar questão', 'add_pergunta'),View('Adicionar resposta', 'add_resposta_1')),View('Adicionar eleitores', 'add_eleitores'),View('Abrir eleição', 'abrir_eleicao'),View('Encerrar eleição','encerrar_eleicao'),View('Apurar eleição','apurar_eleicao'), View('Mostrar resultados','eleicao_para_resultado'), View('Votar', 'eleicoes_para_votar'),View('Logout', 'logout')]
    return menu

@nav.navigation()
def meunavbar_autenticado_comum():
    '''
    Padronização da barra de navegação para as páginas do usuário comum.
    :return:
    '''
    menu = Navbar('BCD29008 - Banco de dados')
    menu.items = [View('Votar', 'eleicoes_para_votar_comum'),View('Logout', 'logout')]
    return menu

@app.route('/')
def inicio():
    '''
    Página inicial.
    :return:
    '''
    if session.get('autenticado'):
        if session.get('admin'):
            return redirect(url_for('autenticado_admin'))
        else:
            return redirect(url_for('autenticado_comum'))

    return render_template('index.html')

@app.route('/login', methods=['GET', 'POST'])
def autenticar():
    '''
    Autenticação do usuario e redirecionamento de página
    :return:
    '''
    # Verifica se está autenticado
    if session.get('autenticado'):
        if session.get('admin'):
            return redirect(url_for('autenticado_admin'))
        else:
            return redirect(url_for('autenticado_comum'))

    # Não está autenticado
    else:
        form = LoginForm()

        if request.method == 'GET':
            return render_template('login.html', form=form)

        if form.validate_on_submit():

            login_pessoa     = form.login_pessoa.data
            password     = form.password.data
            admin_button = form.admin_button.data

            verifica_pessoa = Pessoa.query.filter_by(loginPessoa=login_pessoa).first()

            # Verifica username
            if verifica_pessoa is not None:

                # Verifica senha
                if password == verifica_pessoa.senhaPessoa:

                    # Verificar se informou que é admin
                    if admin_button:

                        pessoa_admin = Pessoa.query.filter_by(loginPessoa=login_pessoa, admin=True).first()

                        if pessoa_admin is not None:
                            session['admin'] = True
                            session['login_pessoa'] = verifica_pessoa.loginPessoa
                            session['name'] = verifica_pessoa.nomePessoa
                            session['autenticado'] = True
                            return redirect(url_for('autenticado_admin'))
                        else:
                            flash("O usuário não é administrador!")
                            return redirect(url_for('autenticar'))
                    else:
                        # Logando como usuário comum
                        session['admin'] = False
                        session['login_pessoa'] = verifica_pessoa.loginPessoa
                        session['name'] = verifica_pessoa.nomePessoa
                        session['autenticado'] = True
                        return redirect(url_for('autenticado_comum'))
                else:
                    flash("Senha incorreta!")
                    return redirect(url_for('autenticar'))
            else:
                flash("Usuário não encontrado!")
                return redirect(url_for('autenticar'))

@app.route('/autenticado_admin', methods=['GET', 'POST'])
def autenticado_admin():
    '''
    Página inicial do adminsitrador.
    :return:
    '''
    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))

    return render_template('autenticado_admin.html')

@app.route('/autenticado_comum', methods=['GET', 'POST'])
def autenticado_comum():
    '''
    Página inicial do usuário comum.
    :return:
    '''
    if not session.get('autenticado'):
        flash("Você precisa estar autenticado para acessar essa página!")
        return redirect(url_for('autenticar'))

    return render_template('autenticado_comum.html')

@app.route('/criar_eleicao', methods=['GET', 'POST'])
def criar_eleicao():
    '''
    Método responsável pela criação de uma eleição.
    :return:
    '''
    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:
            form = CriarEleicaoFrom()
            if request.method == 'GET':
                return render_template('criar_eleicao.html', form=form)

            if form.validate_on_submit():
                nome_eleicao = form.nomeEleicao.data

                verifica_nome_eleicao = Eleicao.query.filter_by(nomeEleicao=nome_eleicao).first()

                if verifica_nome_eleicao is not None:
                    flash("Já existe uma eleição com esse nome!")
                    return redirect(url_for('criar_eleicao'))
                else:
                    now = datetime.now()
                    data = now.strftime("%m/%d/%Y, %H:%M:%S")

                    eleicao = Eleicao(loginPessoa=session.get('login_pessoa'), nomeEleicao=nome_eleicao, inicioEleicao=data, estadoEleicao=False, apuradaEleicao=False)
                    db.session.add(eleicao)
                    db.session.commit()
                    flash("Eleição criada!")

                    eleicao_info = Eleicao.query.filter_by(nomeEleicao=nome_eleicao).first()
                    session['id_eleicao'] = eleicao_info.idEleicao
                    session['nome_eleicao'] = eleicao_info.nomeEleicao

                    return redirect(url_for('add_pergunta'))

@app.route('/add_pergunta', methods=['GET', 'POST'])
def add_pergunta():
    '''
    Método que adiciona uma pergunta/questão a uma eleição.
    :return:
    '''
    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:
            form = AdicionarPerguntaForm()

            eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=False, apuradaEleicao=False).all()
            if len(eleicoes) == 0:
                flash("Você não possui eleições para cadastrar perguntas!")
                return redirect(url_for('autenticado_admin'))

            # Se não criou nenhuma eleicao durante a sessão, session.get('id_eleicao') vazio
            if session.get('id_eleicao') is None:
                eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=False, apuradaEleicao=False).all()

                if eleicoes is None:
                    return redirect(url_for('autenticado_admin'))

                for eleicao in eleicoes:
                    form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))
            # Se possui algo em session.get('id_eleicao')
            else:
                form.eleicao.choices = [(session.get('id_eleicao'), session.get('nome_eleicao'))]
                eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=False, apuradaEleicao=False).all()

                if eleicoes is None:
                    return redirect(url_for('autenticado_admin'))

                eleicoes_sem_id_eleicao = []
                for eleicao in eleicoes:
                    if eleicao.idEleicao != session.get('id_eleicao'):
                        eleicoes_sem_id_eleicao.append(eleicao)

                for eleicao in eleicoes_sem_id_eleicao:
                    form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

            if request.method == 'GET':
                return render_template('add_pergunta.html', form=form)

            if form.validate_on_submit():

                nome_pergunta = form.nomePergunta.data
                min_respostas = form.minRespostas.data
                max_respostas = form.maxRespostas.data

                session['id_eleicao'] = form.eleicao.data

                if min_respostas == 0 or max_respostas == 0:
                    flash("")
                    return render_template('add_pergunta.html', form=form)

                if min_respostas > max_respostas:
                    flash("O número mínimo de respostas deve ser menor que o número máximo!")
                    return render_template('add_pergunta.html', form=form)

                verifica_pergunta_eleicao = Pergunta.query.filter_by(idEleicao=session.get('id_eleicao'), pergunta=nome_pergunta).first()

                if verifica_pergunta_eleicao is not None:
                    flash("Essa questão já está inserida nessa eleição!")
                    return render_template('add_pergunta.html', form=form)
                else:
                    pergunta = Pergunta(idEleicao=session.get('id_eleicao'), pergunta=nome_pergunta, minRespostas=min_respostas, maxRespostas=max_respostas)
                    db.session.add(pergunta)
                    db.session.commit()
                    flash("Pergunta adicionada!")

                    pergunta_info = Pergunta.query.filter_by(pergunta=nome_pergunta).first()
                    session['id_pergunta'] = pergunta_info.idPergunta
                    session['nome_pergunta'] = pergunta_info.pergunta

                    session.pop('id_eleicao', None)
                    return render_template('add_pergunta.html', form=form)
            else:
                flash("Números de respostas inconsistentes!")
                session.pop('id_eleicao', None)
                return render_template('add_pergunta.html', form=form)

@app.route('/add_resposta_1', methods=['GET', 'POST'])
def add_resposta_1():
    '''
    Método que adiciona uma resposta a uma questão/pergunta.
    Seleciona a eleição a qual deseja inserir uma resposta a uma pergunta.
    :return:
    '''
    # Verifica se está autenticado
    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        # Verifica se é admin
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:
            form = EscolherEleicaoForm()
            session['id_eleicao'] = None

            # Filtra as eleições do usuário autenticado
            eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=False, fimEleicao=None, apuradaEleicao=False).all()

            # Só mostrar as eleicoes que tem perguntas
            eleicoes_com_perguntas = []
            for eleicao in eleicoes:
                perguntas = Pergunta.query.filter_by(idEleicao=eleicao.idEleicao).first()
                if perguntas is not None:
                    eleicoes_com_perguntas.append(eleicao)

            if not eleicoes_com_perguntas:
                flash("Não há eleições com perguntas para adicionar respostas!")
                return redirect(url_for('autenticado_admin'))

            else:

                for eleicao in eleicoes_com_perguntas:
                    form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

            if request.method == 'GET':
                return render_template('add_resposta_1.html', form=form)

            if form.validate_on_submit():
                session['id_eleicao'] = form.eleicao.data
                return redirect(url_for('add_resposta_2'))

@app.route('/add_resposta_2', methods=['GET', 'POST'])
def add_resposta_2():
    '''
    Método que adiciona uma resposta a uma questão/pergunta.
    Seleciona a pergunta a qual deseja adicionar uma resposta.
    :return:
    '''
    # Verifica se tem um id_eleicao
    if not session.get('id_eleicao'):
        return redirect(url_for('autenticado_admin'))

    # Verifica se está autenticado
    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        # Verifica se é admin
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:
            form = EscolherPerguntaAddRespostaForm()

            # Filtra as perguntas da eleição escolhida
            perguntas = Pergunta.query.filter_by(idEleicao=session.get('id_eleicao')).all()

            for pergunta in perguntas:
                    form.pergunta.choices.append((pergunta.idPergunta,pergunta.pergunta))

            if request.method == 'GET':
                return render_template('add_resposta_2.html', form=form)

            if form.validate_on_submit():
                nome_resposta = form.resposta.data
                id_pergunta = form.pergunta.data

                # Verificar se essa resposta já existe para essa pergunta na eleicao eleicao
                verifica_resposta_pergunta = Resposta.query.filter_by(resposta=nome_resposta, idEleicao=session.get('id_eleicao'), idPergunta=id_pergunta).first()

                if verifica_resposta_pergunta is not None:
                    flash("Essa resposta já foi cadastrada para essa pergunta!")
                    return render_template('add_resposta_1.html', form=form)

                else:
                    #Verifica se ja tem a resposta "ranco"
                    verifica_resposta_branco = Resposta.query.filter_by(idPergunta=id_pergunta, idEleicao=session.get('id_eleicao'), resposta="Branco").first()
                    if not verifica_resposta_branco:
                        resposta = Resposta(idPergunta=id_pergunta, idEleicao=session.get('id_eleicao'),resposta="Branco", votoResposta=0)
                        db.session.add(resposta)
                        db.session.commit()

                    resposta = Resposta(idPergunta=id_pergunta, idEleicao=session.get('id_eleicao'), resposta=nome_resposta, votoResposta=0)
                    db.session.add(resposta)
                    db.session.commit()
                    flash("Resposta adicionada")

                    session.pop('id_eleicao', None)
                    session.pop('pergunta', None)
                    return redirect(url_for('autenticado_admin'))

@app.route('/add_eleitores', methods=['GET', 'POST'])
def add_eleitores():
    '''
    Método responsável pela escolha da eleição para adicionar eleitores.
    :return:
    '''
    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:

            form = EscolherEleicaoForm()
            session['id_eleicao'] = None

            eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=False, fimEleicao=None, apuradaEleicao=False).all()

            if not eleicoes:
                flash('Não há eleicões para inserir eleitores!')
                return redirect(url_for('autenticado_admin'))

            for eleicao in eleicoes:
                form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

            if request.method == 'GET':
                return render_template('add_eleitores.html', form=form)

            if form.validate_on_submit():
                session['id_eleicao'] = form.eleicao.data
                return redirect(url_for('escolher_eleitores'))

@app.route('/escolher_eleitores', methods=['GET', 'POST'])
def escolher_eleitores():
    '''
    Método responsável pela escolha dos eleitores a serem inseridos em uma eleição.
    :return:
    '''
    # Verifica se tem um id_eleicao
    if not session.get('id_eleicao'):
        return redirect(url_for('autenticado_admin'))

    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:
            form = EscolherEleitoresForm()

            if request.method == 'GET':
                return render_template('escolher_eleitores.html', form=form)

            if form.validate_on_submit():
                eleitores_1 = form.eleitores1.data
                eleitores_2 = form.eleitores2.data
                eleitores_3 = form.eleitores3.data

                print(eleitores_1)
                print(eleitores_2)
                print(eleitores_3)

                if eleitores_1:
                    path = 'eleitores1.txt'
                    file = open(path,'r')
                    for login in file:
                        # Verificar se esse login ja está na eleição
                        login = login.strip('\n')
                        eleitor_verifica = Eleitor.query.filter_by(loginPessoa=login, idEleicao=session.get('id_eleicao')).first()
                        print(eleitor_verifica)
                        if not eleitor_verifica:
                            eleitor = Eleitor(loginPessoa=login, idEleicao=session.get('id_eleicao'), statusEleitor=False)
                            db.session.add(eleitor)
                            db.session.commit()
                    file.close()

                if eleitores_2:
                    path = 'eleitores2.txt'
                    file = open(path, 'r')
                    for login in file:
                        login = login.strip('\n')
                        # Verificar se esse login ja está na eleição
                        eleitor_verifica = Eleitor.query.filter_by(loginPessoa=login, idEleicao=session.get('id_eleicao')).first()
                        if not eleitor_verifica:
                            eleitor = Eleitor(loginPessoa=login, idEleicao=session.get('id_eleicao'), statusEleitor=False)
                            db.session.add(eleitor)
                            db.session.commit()
                    file.close()

                if eleitores_3:
                    path = 'eleitores3.txt'
                    file = open(path, 'r')
                    for login in file:
                        login = login.strip('\n')
                        # Verificar se esse login ja está na eleição
                        eleitor_verifica = Eleitor.query.filter_by(loginPessoa=login, idEleicao=session.get('id_eleicao')).first()
                        if not eleitor_verifica:
                            eleitor = Eleitor(loginPessoa=login, idEleicao=session.get('id_eleicao'), statusEleitor=False)
                            db.session.add(eleitor)
                            db.session.commit()
                    file.close()

                session.pop('id_eleicao', None)
                flash("Eleitores adicionados!")

                return redirect(url_for('autenticado_admin'))

@app.route('/abrir_eleicao', methods=['GET', 'POST'])
def abrir_eleicao():
    '''
    Abrir uma eleição, liberar votação
    :return:
    '''
    if not session.get('autenticado'):
        flash("Você precisa estar autenticado para acessar essa página!")
        return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))

    form = AbrirEleicaoForm()
    session['id_eleicao'] = None

    eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=False, fimEleicao=None, apuradaEleicao=False).all()

    if not eleicoes:
        flash("Não há eleições para abrir!")
        return redirect(url_for('autenticado_admin'))

    for eleicao in eleicoes:
        form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

    if request.method == 'GET':
        return render_template('abrir_eleicao.html', form=form)

    if form.validate_on_submit():

        eleitores = Eleitor.query.filter_by(idEleicao=form.eleicao.data).all()

        if not eleitores:
            flash('Essa eleição não possui eleitores cadastrados!')
            return redirect(url_for('abrir_eleicao'))

        perguntas = Pergunta.query.filter_by(idEleicao=form.eleicao.data).all()

        if not perguntas:
            flash('A leição não possui perguntas cadastradas!')
            return redirect(url_for('abrir_eleicao'))

        for pergunta in perguntas:
            id_pergunta = pergunta.idPergunta
            nome_pergunta = pergunta.pergunta
            max_respostas_teoria = pergunta.maxRespostas

            respostas = Resposta.query.filter_by(idPergunta=id_pergunta).all()
            # Remove pois 1 resposta vai ser sempre "Branco"
            max_respostas_pratica = (len(respostas) - 1)

            if max_respostas_pratica < max_respostas_teoria:
                respostas_faltando = max_respostas_teoria - max_respostas_pratica
                flash("Você deve cadastrar " + str(respostas_faltando) + " respostas para a pergunta: " + nome_pergunta)
                session['id_pergunta'] = id_pergunta
                return redirect(url_for('abrir_eleicao'))

    now = datetime.now()
    data = now.strftime("%m/%d/%Y, %H:%M:%S")

    eleicao = Eleicao.query.filter_by(idEleicao=form.eleicao.data).first()
    eleicao.inicioEleicao = data
    eleicao.estadoEleicao = True

    db.session.add(eleicao)
    db.session.commit()

    session.pop('id_eleicao', None)
    session.pop('id_pergunta', None)
    session.pop('id_resposta', None)

    flash("Eleição aberta!")

    return redirect(url_for('autenticado_admin'))

@app.route('/eleicoes_para_votar', methods=['GET', 'POST'])
def eleicoes_para_votar():
    '''
    Método responsável por mostrar as eleições as quais o usuário pode votar a fim de serem exibidas para que o mesmo escolha uma.
    :return:
    '''
    if not session.get('autenticado'):
        flash("Você precisa estar autenticado para acessar essa página!")
        return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))

    form = EscolherEleicaoForm()
    session.pop('id_eleicao', None)
    session.pop('id_pergunta', None)

    # Eleicões abertas no geral
    eleicoes_abetas = Eleicao.query.filter_by(estadoEleicao=True, fimEleicao=None, apuradaEleicao=False).all()

    if not eleicoes_abetas:
        flash("Nenhuma eleição aberta no momento!")
        return redirect(url_for('autenticado_admin'))
    else:
        for eleicao in eleicoes_abetas:
            form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

    if request.method == 'GET':
        return render_template('eleicoes_para_votar.html', form=form)

    if form.validate_on_submit():
        session['id_eleicao'] = form.eleicao.data
        return redirect(url_for('votar_em_eleicao'))

@app.route('/votar_em_eleicao', methods=['GET', 'POST'])
def votar_em_eleicao():
    '''
    Método responsável por realizar o voto de um usuário administrador.
    :return:
    '''
    # Verifica se tem um id_eleicao
    if not session.get('id_eleicao'):
        return redirect(url_for('eleicoes_para_votar'))

    if not session.get('autenticado'):
        flash("Você precisa estar autenticado para acessar essa página!")
        return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))

    pessoa_nome = session.get('login_pessoa')
    eleicao_id = session.get('id_eleicao')

    eleitor = Eleitor.query.filter_by(loginPessoa=pessoa_nome, idEleicao=eleicao_id).first()

    if eleitor is None:
        flash("Você não pode votar nessa eleição!")
        return redirect(url_for('eleicoes_para_votar'))

    else:
        #Verifica se o status é False
        status_eleitor = eleitor.statusEleitor

        if status_eleitor:
            flash("Você já votou nessa eleição!")
            return redirect(url_for('eleicoes_para_votar'))

        else:
            eleicao = Eleicao.query.filter_by(idEleicao=session.get('id_eleicao')).first()
            nome_eleicao = eleicao.nomeEleicao

            perguntas = Pergunta.query.filter_by(idEleicao=session.get('id_eleicao')).all()

            lista_questoes_respostas = []

            for pergunta in perguntas:
                id_pergunta = pergunta.idPergunta
                lista_respostas = []

                respostas = Resposta.query.filter_by(idPergunta=id_pergunta, idEleicao=session.get('id_eleicao')).all()

                for resposta in respostas:
                    lista_respostas.append(resposta)

                tupla_questao_respostas = (pergunta, lista_respostas)

                lista_questoes_respostas.append(tupla_questao_respostas)

    form = MostraResultadosForm()

    if request.method == 'GET':
        return render_template('votar_em_eleicao.html', lista_questoes=lista_questoes_respostas, tituloEleicao=nome_eleicao, form=form)

    else:
        qtd_perguntas_respondidas = []

        resultado = list(request.form.items())
        resultado.pop() # Removendo o ultimo item

        if not resultado:
            flash("Você deve votar em todas as questões e respeitar os limites de mínimo e máximo de respostas!")
            return redirect(url_for('autenticado_admin'))

        for itens in resultado:
            n_questao = itens[0].split('-')[0]
            qtd_perguntas_respondidas.append(n_questao)

        c = Counter(qtd_perguntas_respondidas)

        # Verificar se deixou de resposnder a alguma pergunta
        if len(c.values()) != len(perguntas):
            print("Você deve votar em todas as questões e respeitar os limites de mínimo e máximo de respostas!")
            return redirect(url_for('eleicoes_para_votar'))
        else:
            # Verificar a quantidade de respostas para cada uma das perguntas
            indice = 0

            for pergunta in perguntas:
                min = pergunta.minRespostas
                max = pergunta.maxRespostas

                qtd_respostas = list(c.values())[indice]
                indice = indice + 1

                # print("----------------------")
                # print("Pergunta: " + pergunta.pergunta)
                # print("----------------------")
                # print("Mínimo: " + str(min))
                # print("Máximo: " + str(max))
                # print("----------------------")
                # print("Qtd de respostas: " + str(qtd_respostas))
                # print("----------------------")

                if qtd_respostas > max or qtd_respostas < min:
                    flash("Voceê deve respeitar os limites de mínimo e máximo de respostas!")
                    session.pop('id_eleicao', None)
                    session.pop('id_pergunta', None)
                    return redirect(url_for('eleicoes_para_votar'))

            for itens in resultado:
                num_resposta = itens[0].split('-')[1]
                num = int(num_resposta)

                resposta = Resposta.query.filter_by(idEleicao=session.get('id_eleicao'), idResposta=num).first()
                resposta.votoResposta = resposta.votoResposta + 1
                db.session.add(resposta)
                db.session.commit()

            pessoa_nome = session.get('login_pessoa')
            eleicao_id = session.get('id_eleicao')

            # eleitor = Eleitor.query.filter_by(idEleicao=eleicao_id, loginPessoa=pessoa_nome).first()
            # print(eleitor.loginPessoa)

            Eleitor.query.filter_by(idEleicao=eleicao_id, loginPessoa=pessoa_nome).delete(synchronize_session=False)
            db.session.commit()

            flash("Votou!")

            session.pop('id_eleicao', None)
            session.pop('id_pergunta', None)
            session.pop('id_resposta', None)

            return redirect(url_for('autenticado_admin'))

##

@app.route('/eleicoes_para_votar_comum', methods=['GET', 'POST'])
def eleicoes_para_votar_comum():
    '''
    Método responsável por mostrar as eleições as quais o usuário pode votar a fim de serem exibidas para que o mesmo escolha uma.
    :return:
    '''
    if not session.get('autenticado'):
        flash("Você precisa estar autenticado para acessar essa página!")
        return redirect(url_for('autenticar'))

    form = EscolherEleicaoForm()
    session.pop('id_eleicao', None)
    session.pop('id_pergunta', None)

    # Eleicões abertas no geral
    eleicoes_abetas = Eleicao.query.filter_by(estadoEleicao=True, fimEleicao=None, apuradaEleicao=False).all()

    if not eleicoes_abetas:
        flash("Nenhuma eleição aberta no momento!")
        return redirect(url_for('autenticado_comum'))
    else:
        for eleicao in eleicoes_abetas:
            form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

    if request.method == 'GET':
        return render_template('eleicoes_para_votar_comum.html', form=form)

    if form.validate_on_submit():
        session['id_eleicao'] = form.eleicao.data

        return redirect(url_for('votar_em_eleicao_comum'))

@app.route('/votar_em_eleicao_comum', methods=['GET', 'POST'])
def votar_em_eleicao_comum():
    '''
       Método responsável por realizar o voto de um usuário comum.
       :return:
       '''
    # Verifica se tem um id_eleicao
    if not session.get('id_eleicao'):
        return redirect(url_for('eleicoes_para_votar_comum'))

    if not session.get('autenticado'):
        flash("Você precisa estar autenticado para acessar essa página!")
        return redirect(url_for('autenticar'))

    pessoa_nome = session.get('login_pessoa')
    eleicao_id = session.get('id_eleicao')

    eleitor = Eleitor.query.filter_by(loginPessoa=pessoa_nome, idEleicao=eleicao_id).first()

    if eleitor is None:
        flash("Você não pode votar nessa eleição!")
        return redirect(url_for('eleicoes_para_votar_comum'))

    else:
        #Verifica se o status é False
        status_eleitor = eleitor.statusEleitor

        if status_eleitor:
            flash("Você já votou nessa eleição!")
            return redirect(url_for('eleicoes_para_votar_comum'))

        else:
            eleicao = Eleicao.query.filter_by(idEleicao=session.get('id_eleicao')).first()
            nome_eleicao = eleicao.nomeEleicao

            perguntas = Pergunta.query.filter_by(idEleicao=session.get('id_eleicao')).all()

            lista_questoes_respostas = []

            for pergunta in perguntas:
                id_pergunta = pergunta.idPergunta
                lista_respostas = []

                respostas = Resposta.query.filter_by(idPergunta=id_pergunta, idEleicao=session.get('id_eleicao')).all()

                for resposta in respostas:
                    lista_respostas.append(resposta)

                tupla_questao_respostas = (pergunta, lista_respostas)

                lista_questoes_respostas.append(tupla_questao_respostas)

    form = MostraResultadosForm()

    if request.method == 'GET':
        return render_template('votar_em_eleicao_comum.html', lista_questoes=lista_questoes_respostas, tituloEleicao=nome_eleicao, form=form)

    else:
        qtd_perguntas_respondidas = []

        resultado = list(request.form.items())
        resultado.pop() # Removendo o ultimo item

        if not resultado:
            flash("Você deve votar em todas as questões e respeitar os limites de mínimo e máximo de respostas!")
            return redirect(url_for('eleicoes_para_votar_comum'))

        for itens in resultado:
            n_questao = itens[0].split('-')[0]
            qtd_perguntas_respondidas.append(n_questao)

        c = Counter(qtd_perguntas_respondidas)

        # Verificar se deixou de resposnder a alguma pergunta
        if len(c.values()) != len(perguntas):
            print("Você deve votar em todas as questões e respeitar os limites de mínimo e máximo de respostas!")
            return redirect(url_for('eleicoes_para_votar_comum'))
        else:
            # Verificar a quantidade de respostas para cada uma das perguntas
            indice = 0

            for pergunta in perguntas:
                min = pergunta.minRespostas
                max = pergunta.maxRespostas

                qtd_respostas = list(c.values())[indice]
                indice = indice + 1

                # print("----------------------")
                # print("Pergunta: " + pergunta.pergunta)
                # print("----------------------")
                # print("Mínimo: " + str(min))
                # print("Máximo: " + str(max))
                # print("----------------------")
                # print("Qtd de respostas: " + str(qtd_respostas))
                # print("----------------------")

                if qtd_respostas > max or qtd_respostas < min:
                    flash("Voceê deve respeitar os limites de mínimo e máximo de respostas!")
                    session.pop('id_eleicao', None)
                    session.pop('id_pergunta', None)
                    return redirect(url_for('eleicoes_para_votar_comum'))

            for itens in resultado:
                num_resposta = itens[0].split('-')[1]
                num = int(num_resposta)

                resposta = Resposta.query.filter_by(idEleicao=session.get('id_eleicao'), idResposta=num).first()
                resposta.votoResposta = resposta.votoResposta + 1
                db.session.add(resposta)
                db.session.commit()

            # return redirect(url_for('atualiza_status'))

            pessoa_nome = session.get('login_pessoa')
            eleicao_id = session.get('id_eleicao')

            # eleitor = Eleitor.query.filter_by(idEleicao=eleicao_id, loginPessoa=pessoa_nome).first()
            # print(eleitor.loginPessoa)

            Eleitor.query.filter_by(idEleicao=eleicao_id, loginPessoa=pessoa_nome).delete(synchronize_session=False)
            db.session.commit()


            flash("Votou!")

            session.pop('id_eleicao', None)
            session.pop('id_pergunta', None)
            session.pop('id_resposta', None)

            return redirect(url_for('autenticado_comum'))

##

@app.route('/encerrar_eleicao', methods=['GET', 'POST'])
def encerrar_eleicao():
    '''
    Método que encerra uma eleição
    :return:
    '''
    if not session.get('autenticado'):
        flash("Você precisa estar autenticado para acessar essa página!")
        return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))

    form = EncerrarEleicaoForm()
    session['id_eleicao'] = None

    eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=True, fimEleicao=None, apuradaEleicao=False).all()

    if not eleicoes:
        flash('Não há eleicões para serem ecerradas!')
        return redirect(url_for('autenticado_admin'))

    for eleicao in eleicoes:
        form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

    if request.method == 'GET':
        return render_template('encerrar_eleicao.html', form=form)

    if form.validate_on_submit():
        now = datetime.now()
        data = now.strftime("%m/%d/%Y, %H:%M:%S")

        eleicao = Eleicao.query.filter_by(idEleicao=form.eleicao.data).first()
        eleicao.fimEleicao = data
        eleicao.estadoEleicao = False

        db.session.commit()
        flash("Eleição encerrada!")

        session.pop('id_eleicao', None)
        session.pop('id_pergunta', None)
        session.pop('id_resposta', None)

        return redirect(url_for('autenticado_admin'))

@app.route('/apurar_eleicao', methods=['GET', 'POST'])
def apurar_eleicao():
    '''
    Apurar uma eleição encerrada
    :return:
    '''
    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:
            form = ApurarEleicaoForm()
            session['id_eleicao'] = None

            eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=False, apuradaEleicao=False).all()

            if not eleicoes:
                flash('Não há eleicões para serem apuradas!')
                return redirect(url_for('autenticado_admin'))

            eleicoes_a_serem_apuradas = []

            for eleicao in eleicoes:
                if eleicao.fimEleicao is not None:
                    eleicoes_a_serem_apuradas.append(eleicao)

            if not eleicoes_a_serem_apuradas:
                flash("Não há eleições para apurar!")
                return redirect(url_for('autenticado_admin'))

            else:
                for eleicao in eleicoes_a_serem_apuradas:
                    form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

            if request.method == 'GET':
                return render_template('apurar_eleicao.html', form=form)

            if form.validate_on_submit():
                eleicao = Eleicao.query.filter_by(idEleicao=form.eleicao.data).first()
                eleicao.apuradaEleicao = True

                db.session.commit()
                flash("Eleição apurada!")

                session.pop('id_eleicao', None)
                session.pop('id_pergunta', None)
                session.pop('id_resposta', None)

                return redirect(url_for('autenticado_admin'))

@app.route('/eleicao_para_resultado', methods=['GET', 'POST'])
def eleicao_para_resultado():
    '''
    Método responsável pela escolha da eleição para apresentar o resultado
    :return:
    '''
    if not session.get('autenticado'):
         flash("Você precisa estar autenticado para acessar essa página!")
         return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:

            form = EscolherEleicaoForm()
            session['id_eleicao'] = None

            eleicoes = Eleicao.query.filter_by(loginPessoa=session.get('login_pessoa'), estadoEleicao=False, apuradaEleicao=True).all()

            if not eleicoes:
                flash('Não há eleicões para apresentar o resultado!')
                return redirect(url_for('autenticado_admin'))

            eleicoes_encerradas_apuradas = []
            for eleicao in eleicoes:
                if eleicao.fimEleicao is not None:
                    eleicoes_encerradas_apuradas.append(eleicao)

            for eleicao in eleicoes_encerradas_apuradas:
                form.eleicao.choices.append((eleicao.idEleicao, eleicao.nomeEleicao))

            if request.method == 'GET':
                return render_template('eleicao_para_resultado.html', form=form)

            if form.validate_on_submit():
                session['id_eleicao'] = form.eleicao.data
                return redirect(url_for('mostrar_resultado'))

@app.route('/mostrar_resultado', methods=['GET', 'POST'])
def mostrar_resultado():
    '''
    Método responsável por mostrar os resultados de uma eleição
    :return:
    '''

    # Verifica se tem um id_eleicao
    if not session.get('id_eleicao'):
        return redirect(url_for('autenticado_admin'))

    if not session.get('autenticado'):
        flash("Você precisa estar autenticado para acessar essa página!")
        return redirect(url_for('autenticar'))
    else:
        if not session.get('admin'):
            flash("Você precisa estar logado como administrador para acessar essa página!")
            return redirect(url_for('autenticar'))
        else:

            eleicao = Eleicao.query.filter_by(idEleicao=session.get('id_eleicao'), loginPessoa=session.get('login_pessoa')).first()
            nome_eleicao = eleicao.nomeEleicao

            perguntas = Pergunta.query.filter_by(idEleicao=session.get('id_eleicao')).all()

            lista_questoes = []

            for pergunta in perguntas:
                id_pergunta = pergunta.idPergunta
                lista_respostas = []

                respostas = Resposta.query.filter_by(idPergunta=id_pergunta, idEleicao=session.get('id_eleicao')).all()

                for resposta in respostas:
                    lista_respostas.append(resposta)

                lista_questao_respostas = [pergunta, lista_respostas]
                lista_questoes.append(lista_questao_respostas)

            session.pop('id_eleicao', None)
            session.pop('id_pergunta', None)
            session.pop('id_resposta', None)

    if request.method == 'GET':
        return render_template('mostrar_resultado.html', lista_questoes=lista_questoes, tituloEleicao=nome_eleicao)

@app.route('/logout', methods=['GET', 'POST'])
def logout():
    '''
    Logou de usuário, limpa as variáveis de sessão
    :return:
    '''
    session.pop('id_eleicao', None)
    session.pop('id_pergunta', None)
    session.pop('id_resposta', None)
    session.pop('autenticado', None)
    session.pop('nome_eleicao', None)
    session.pop('criador_eleicao', None)
    session.pop('nome_pergunta', None)
    session.pop('login_pessoa', None)
    return render_template('logout.html', nome=session.get('name'))

@app.errorhandler(404)
def page_not_found(e):
    '''
    Para tratar erros de páginas não encontradas - HTTP 404
    :param e:
    :return:
    '''
    return render_template('404.html'), 404

if __name__ == '__main__':
    #db.create_all()
    #db.drop_all()
    app.run(host='0.0.0.0', debug=True)