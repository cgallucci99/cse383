var URL = 'http://openlibrary.org/search.json?q='

$('#form').submit(openLibSearch);

function openLibSearch() {
    event.preventDefault();
    var search = $('#search').val();
    $.ajax({
        url: encodeURI(URL + search),
        method: 'GET'
    }).done(function(data) {
        // clear results
        $('#results').html("<ul id='resultsList'></ul>");
        for (var i = 0; i < data.docs.length; i++) {
            try {
                $('#resultsList').append('<li class="getCover" isbn="'+data.docs[i].isbn[0]+'">'+data.docs[i].title_suggest+'</li>')
            } catch {
                $('#resultsList').append('<li class="getCover" isbn="">'+data.docs[i].title_suggest+'</li>')
            }
        }
        $('.getCover').click(getCover);
    })
}

function getCover() {
    console.log('clicked');
    var isbn = $(this).attr('isbn');
    var title = $(this).html();
    $('#coverTitle').html(title);
    if (isbn === '') {
        $('#image').attr('src', '')
        $('#noImage').css('display','block')
    } else {
        $('#image').attr('src', 'http://covers.openlibrary.org/b/isbn/'+isbn+'-M.jpg');
        $('#noImage').css('display','none')
    }
    $('#cover').css('display','block')
}
    