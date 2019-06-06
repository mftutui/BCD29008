from sqlalchemy import create_engine
from sqlalchemy.ext.automap import automap_base
from sqlalchemy.orm import sessionmaker

if __name__ == '__main__':
    print("Exemplo 2")

    engine = create_engine("sqlite:///lab05-ex02.sqlite")

    Session = sessionmaker(bind=engine)
    session = Session()

    Base = automap_base()
    Base.prepare(engine, reflect=True)

    #Tabelas existentes no meu banco
    Pessoa = Base.classes.Pessoa
    Telefones = Base.classes.Telefones
    resultado = session.query(Pessoa).all()

    def listaTudo():
        for linha in resultado:
            print('Id: {}, nome: {}'.format(linha.idPessoa, linha.nome))

            res_telefone = session.query(Telefones).filter(Telefones.idPessoa == Pessoa.idPessoa).all()

            for tel in res_telefone:
                print('tel: {}'.format(tel.numero))

    def listaJ():
        resultado = session.query(Pessoa).filter(Pessoa.nome.ilike("J%")).all()

        for linha in resultado:
            print('Id: {}, nome: {}'.format(linha.idPessoa, linha.nome))


    def listaTelefones():
        resultado = session.query(Pessoa).join(Telefones).all()

        for linha in resultado:
            print('Id: {}, nome: {}'.format(linha.idPessoa, linha.nome))
            for tel in linha.telefones_collection:
                print('tel: {}'.format(tel.numero))

    listaTelefones()