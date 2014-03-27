Para que funcione la autenticación, modificar el archivo standalone.xml

agregar dentro del tag de security el siguiente security-domain:

<subsystem xmlns="urn:jboss:domain:security:1.1">
	<security-domains>
		...
		...
		...
                <security-domain name="bookmark" cache-type="default">
                    <authentication>
                        <login-module code="Database" flag="required">
                            <module-option name="dsJndiName" value="java:jboss/datasources/ExampleDS"/>
                            <module-option name="principalsQuery" value="select usuario0_.pwd from Usuario usuario0_ where usuario0_.username=?"/>
                            <module-option name="rolesQuery" value="select rol0_.descripcion, 'Roles' from Usuario usuario0_,Rol rol0_ where usuario0_.rolId=rol0_.rolId and usuario0_.username=?"/>
                            <module-option name="hashAlgorithm" value="MD5"/>
                            <module-option name="hashEncoding" value="hex"/>
                        </login-module>
                    </authentication>
                </security-domain>
		...
		...
		...
	</security-domains>
 </subsystem>

