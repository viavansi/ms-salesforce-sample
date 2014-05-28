Mobile Services - Integración con Salesforce
==========

En este ejemplo de integración se obtiene el listado de <code>Accounts</code> de Salesforce usando la versión Partner de la <a href="http://www.salesforce.com/us/developer/docs/api/">SOAP API</a> y el listado de dispositivos registrados en Mobile Services para el usuario seleccionado usando la <a href="https://github.com/viavansi/ms-sdk-java">SDK Java</a>

Al pulsar el botón "Send" el dispositivo seleccionado recibe una notificación push con el documento relleno con los datos obtenidos en Salesforce. Se completa el documento con la foto y la firma biométrica en el dispositivo y se realiza la firma en servidor.

Una vez completada la firma la aplicación recive una respuesta asíncrona con el identificador de firma y finalmente registra en documento para que pueda ser consultado desde la pestaña <code>Documents</code> en Salesforce.

Para una integración completa, esta aplicación se puede añadir en Salesforce usando una <a href="https://help.salesforce.com/HTViewHelpDoc?id=dev_tabscreate.htm">Custom Tab Definition</a>.

No olvide modificar sus parámetros de configuración en la clase <code>com.viafirma.documents.sample.SampleConfig</code>

