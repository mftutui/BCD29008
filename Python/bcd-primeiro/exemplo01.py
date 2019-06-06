
import sqlite3

def buscarPessoa():
    pessoa = ('Juca',)

    cursor.execute("SELECT * FROM Pessoa WHERE  nome = ?", pessoa)

    for linha in cursor.fetchall():
        print('Id: {}, nome: {}'.format(linha[0], linha[1]))

    cursor.close()

if __name__ == '__main__':
    print("Olá Maria")

    conexao = sqlite3.connect("lab05-ex01.sqlite")

    cursor = conexao.cursor()

    cursor.execute("SELECT * FROM Pessoa")

    print(cursor.fetchall())

    buscarPessoa()


