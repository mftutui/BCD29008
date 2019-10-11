from flask_sqlalchemy import SQLAlchemy
from flask import Flask
from flask_bootstrap import Bootstrap
from flask_nav import Nav

from flask import Flask, render_template, flash, redirect, url_for, request, session
from flask_sqlalchemy import SQLAlchemy
from werkzeug.security import generate_password_hash, check_password_hash
from flask import Flask, render_template
from flask_bootstrap import Bootstrap
from flask_nav import Nav
from flask_nav.elements import Navbar, View, Subgroup, Link
from werkzeug.security import generate_password_hash, check_password_hash

SECRET_KEY = 'string aleatória'

app = Flask(__name__)
app.secret_key = 'SECRET_KEY'

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///projeto2.sqlite'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS']= False
db = SQLAlchemy(app)
boostrap = Bootstrap(app)
nav = Nav()
nav.init_app(app)

class Pessoa(db.Model):
    __tablename__       = 'Pessoa'

    loginPessoa         = db.Column(db.String(45), primary_key=True, nullable=False)
    nomePessoa          = db.Column(db.String(45), nullable=False)
    senhaPessoa         = db.Column(db.String(45), nullable=False)
    admin               = db.Column(db.Boolean, nullable=False)

    def _init_(self, **kwargs):
        super()._init_(**kwargs)
        self.loginPessoa        = kwargs.pop('loginPessoa')
        self.nomePessoa         = kwargs.pop('nomePessoa')
        self.senhaPessoa        = kwargs.pop('senhaPessoa')
        self.admin              = kwargs.pop('admin')

class Eleicao(db.Model):
    __tablename__       = 'Eleicao'

    idEleicao           = db.Column(db.Integer, primary_key=True, autoincrement=True)
    loginPessoa         = db.Column(db.String(45), db.ForeignKey('Pessoa.loginPessoa'), nullable=False)
    nomeEleicao         = db.Column(db.String(45), nullable=False)
    inicioEleicao       = db.Column(db.String(45), nullable=False)
    fimEleicao          = db.Column(db.String(45), nullable=True)
    estadoEleicao       = db.Column(db.Boolean, nullable=False)
    apuradaEleicao      = db.Column(db.Boolean, nullable=False)

    def _init_(self, **kwargs):
        super()._init_(**kwargs)
        self.nomeEleicao        = kwargs.pop('nomeEleicao')
        self.loginPessoa        = kwargs.pop('loginPessoa')
        self.inicioEleicao      = kwargs.pop('inicioEleicao')
        self.estadoEleicao      = False
        self.apuradaEleicao     = False


class Eleitor(db.Model):
    __tablename__       = "Eleitor"

    loginPessoa         = db.Column(db.String(45), db.ForeignKey('Pessoa.loginPessoa'), nullable=False)
    idEleicao           = db.Column(db.Integer, db.ForeignKey('Eleicao.idEleicao'), primary_key=True, nullable=False)
    statusEleitor       = db.Column(db.Boolean, nullable=False)

    def init(self, **kwargs):
        super()._init_(**kwargs)
        self.loginPessoa        = kwargs.pop('loginPessoa')
        self.idEleicao          = kwargs.pop('idEleicao')
        self.statusEleitor      = False


class Pergunta(db.Model):
    __tablename__       = "Pergunta"

    idPergunta          = db.Column(db.Integer, primary_key=True, autoincrement=True, nullable=False)
    idEleicao           = db.Column(db.Integer, db.ForeignKey('Eleicao.idEleicao'), nullable=False)
    pergunta            = db.Column(db.String(45), nullable=False)
    minRespostas        = db.Column(db.Integer, nullable=False)
    maxRespostas        = db.Column(db.Integer, nullable=False)

    def init(self, **kwargs):
        super().__init__(**kwargs)
        self.idEleicao          = kwargs.pop('idEleicao')
        self.pergunta           = kwargs.pop('pergunta')
        self.minRespostas       = kwargs.pop('minRespostas')
        self.maxRespostas       = kwargs.pop('maxRespostas')


class Resposta(db.Model):
    __tablename__       = "Resposta"

    idResposta          = db.Column(db.Integer, primary_key=True, autoincrement=True, nullable=False)
    idPergunta          = db.Column(db.Integer, db.ForeignKey('Pergunta.idPergunta'), nullable=False)
    idEleicao           = db.Column(db.Integer, db.ForeignKey('Eleicao.idEleicao'), nullable=False)
    resposta            = db.Column(db.String(45), nullable=False)
    votoResposta        = db.Column(db.Integer, nullable=False)

    def init(self, **kwargs):
        super().__init__(**kwargs)
        self.idPergunta         = kwargs.pop('idPergunta')
        self.idEleicao          = kwargs.pop('idEleicao')
        self.resposta           = kwargs.pop('resposta')
        self.votoResposta       = kwargs.pop('votoResposta')