module.exports = async function (context, documents) {
    
    if (!!documents && documents.length > 0) {
        for (var i = 0; i < documents.length; i++) {
            context.log('Document: ', documents[i]); 
        }
    }
}
