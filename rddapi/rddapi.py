from flask import Flask
from flask_restful import Resource, Api, reqparse
import mysql.connector

app = Flask(__name__)
api = Api(app)

host="localhost"
user="marco_galindo"
passwd="marquino"
database="odk_prod"

app = Flask(__name__)
api = Api(app)

mydb = mysql.connector.connect(
	host=host,
	user=user,
	passwd=passwd,
	database=database
)


class versionApi(Resource):
	def get(self):
		return {'apiName':'rddapi','apiVersion':'0.0'}

class addRerporte(Resource):
	def post(self):

		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('centro')
		parser.add_argument('fecha')
		parser.add_argument('actividad')
		parser.add_argument('grado')
		parser.add_argument('perdidos')
		parser.add_argument('materias')
		args = parser.parse_args()
		print(args)
		#validar con base de datos
		response = {'mensaje':'Ingresado correctamente'}
		return response
		
class getDepartamentos(Resource):
	def get(self):
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getDepartamentos')
		deptos = db.stored_results().next().fetchall()
		response = {}
		deptosList = []
		#indice = 0
		for depto in deptos:
			deptoDict = {'id':depto[0],'departamento':depto[1]}
			deptosList.append(deptoDict) 
			#response.update({str(indice):deptoDict})
			#indice=indice+1
		response.update({'departamentos':deptosList})
		return response


class getMunicipios(Resource):
	def post(self):
		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('departamento')
		args = parser.parse_args()
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getMunicipios',[args['departamento']])
		municipios = db.stored_results().next().fetchall()
		response = {}
		munisList = []
		for municipio in  municipios:
			muniDict = {'id_depto':municipio[0],'id_municipio':municipio[1],'municipio':municipio[2]}
			munisList.append(muniDict) 
		response.update({'municipios':munisList})
		return response

class getNiveles(Resource):
	def post(self):
		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('departamento')
		parser.add_argument('municipio')
		args = parser.parse_args()
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getNiveles',[args['departamento'],args['municipio']])
		niveles = db.stored_results().next().fetchall()
		response = {}
		nivelesList = []
		for nivel in  niveles:
			nivelDict = {'id_nivel':nivel[0],'nivel':nivel[1]}
			nivelesList.append(nivelDict)
		response.update({'niveles':nivelesList})
		return response

class getCentros(Resource):
	def post(self):
		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('departamento')
		parser.add_argument('municipio')
		parser.add_argument('nivel')
		args = parser.parse_args()
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('rdd_getCentros',[args['departamento'],args['municipio'],args['nivel']])
		centros = db.stored_results().next().fetchall()
		response = {}
		centrosList = []
		for centro in  centros:
			centroDict = {'codigo':centro[0],'nombre':centro[1],'municipio':centro[2],'direccion':centro[3],'latitud':centro[4],'longitud':centro[5]}
			centrosList.append(centroDict) 
		response.update({'centros':centrosList})
		return response

api.add_resource(versionApi,'/')
api.add_resource(addRerporte,'/addReporte')
api.add_resource(getDepartamentos,'/getDepartamentos')
api.add_resource(getMunicipios,'/getMunicipios')
api.add_resource(getNiveles,'/getNiveles')
api.add_resource(getCentros,'/getCentros')


if (__name__=='__main__'):
	app.run(port = 5100, host = '0.0.0.0') 
