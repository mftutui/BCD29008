from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, SubmitField, IntegerField, SelectField, BooleanField, DateTimeField, BooleanField, RadioField, validators, SelectMultipleField
from wtforms.validators import DataRequired


'''
Veja mais na documentação do WTForms

https://wtforms.readthedocs.io/en/stable/
https://wtforms.readthedocs.io/en/stable/fields.html

Um outro pacote interessante para estudar:

https://wtforms-alchemy.readthedocs.io/en/latest/

'''

class LoginForm(FlaskForm):
    login_pessoa    = StringField('Login', validators=[DataRequired("O preenchimento desse campo é obrigatório")])
    password        = PasswordField('Senha', validators=[DataRequired("O preenchimento desse campo é obrigatório")])
    admin_button    = BooleanField('Administrador', false_values=None)
    submit          = SubmitField('Entrar')


class CriarEleicaoFrom(FlaskForm):
    nomeEleicao     = StringField('Nome da eleição', validators=[DataRequired("O preenchimento desse campo é obrigatório")])
    submit          = SubmitField('Criar')


class AdicionarPerguntaForm(FlaskForm):
    eleicao         = SelectField('Escolha a eleição', choices=[], coerce=int)
    nomePergunta    = StringField('Pergunta', validators=[DataRequired("O preenchimento desse campo é obrigatório")])
    minRespostas    = IntegerField('Número mínimo de respostas', validators=[DataRequired("Esse campo deve ser preenchido com um número")])
    maxRespostas    = IntegerField('Número máximo de respostas', validators=[DataRequired("Esse campo deve ser preenchido com um número")])
    submit          = SubmitField('Adicionar')


class AdicionarRespostaForm(FlaskForm):
    eleicao         = SelectField('Escolha a eleição', choices=[], coerce=int)
    pergunta        = SelectField('Escolha a pergunta', choices=[], coerce=int)
    resposta        = StringField('Resposta', validators=[DataRequired("O preenchimento desse campo é obrigatório")])
    submit          = SubmitField('Adicionar')


class AbrirEleicaoForm(FlaskForm):
    eleicao         = SelectField('Escolha a eleição', choices=[], coerce=int)
    submit          = SubmitField('Abrir')


class EncerrarEleicaoForm(FlaskForm):
    eleicao         = SelectField('Escolha a eleição', choices=[], coerce=int)
    submit          = SubmitField('Encerrar')


class ApurarEleicaoForm(FlaskForm):
    eleicao         = SelectField('Escolha a eleição', choices=[], coerce=int)
    submit          = SubmitField('Apurar')


class VotarForm(FlaskForm):
    eleicao         = SelectField('Escolha a eleição', choices=[], coerce=int)
    submit          = SubmitField('Votar')


class VotarEmEleicaoForm(FlaskForm):
     voto           = BooleanField()
     submit         = SubmitField('Votar')


class EscolherEleicaoForm(FlaskForm):
    eleicao         = SelectField('Escolha a eleição', choices=[], coerce=int)
    submit          = SubmitField('Escolher')


class EscolherPerguntaAddRespostaForm(FlaskForm):
    pergunta        = SelectField('Escolha a pergunta', choices=[], coerce=int)
    resposta        = StringField('Resposta', validators=[DataRequired("O preenchimento desse campo é obrigatório")])
    submit          = SubmitField('Adcionar')


class EscolherEleitoresForm(FlaskForm):
    eleitores1     = BooleanField('Eleitores1', false_values=None)
    eleitores2     = BooleanField('Eleitores2', false_values=None)
    eleitores3     = BooleanField('Eleitores3', false_values=None)
    submit         = SubmitField('Escolher')


class MostraResultadosForm(FlaskForm):
    submit         = SubmitField('Escolher')