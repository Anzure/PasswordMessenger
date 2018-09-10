#Liste over klasser
$groups = 'TELVS-BYN18','TELVS-MAN18','TELVS-AN18','TELVS-BAN18'

# Importerer moduler
Import-Module ActiveDirectory
Import-Module Microsoft.PowerShell.Management
Import-Module Microsoft.PowerShell.Utility
Import-Module ConfigurationManager

# Ordliste
$wordsA = 'Oste','Leke','Velge','Vaske','Trene','Telle','Sykle','Studere','Stemme','Snike','Smile','Skrive','Knekke','Danse','Drikke','Turkis','Gul','Lilla','Svart','Hvit'
$wordsB = 'kake','banan','bil','traktor','fjell','hytte','eple','taco','pizza','blyant','fisk','egg','biff','ball','melk','ost','sko','stol','bord','flaske'

# Objekter for loggføring
$Table = @()
$Record = @{
  "Fornavn" = ""
  "Etternavn" = ""
  "Brukernavn" = ""
  "Passord" = ""
  "Klasse" = ""
  "Telefon" = ""
  "SkoleEpost" = ""
  "Epost" = ""
}

Foreach($GroupName in $groups)
{

	Write-Host ""
	Write-Host "Klasse" $GroupName
	Write-Host ""

    # Endre passord for en gruppe
    $Group = Get-ADGroupMember -Identity $GroupName -Server skole.top.no -Recursive | Get-ADUser -Properties Mail, GivenName, Surname, Mobile
    Foreach($Member in $Group)
    {
        # Endre passord for brukeren
        $wordA = $wordsA[(Get-Random -Maximum ([array]$wordsA).count)]
        $wordB = $wordsB[(Get-Random -Maximum ([array]$wordsB).count)]
        $num = Get-Random -maximum 99 -minimum 10
        $password = $wordA + $wordB + $num
        Set-ADAccountPassword -Server skole.top.no -Identity $Member.samaccountname -Reset -NewPassword (ConvertTo-SecureString -AsPlainText $password -Force)

        # Print til powershell
        Write-Host "Endret passord for:"
        Write-Host $Member.samaccountname
        Write-Host $password
        Write-Host "-------------"

        # Skriv logg til CSV
        $Record."Fornavn" = $Member.givenname
        $Record."Etternavn" = $Member.surname
        $Record."Brukernavn" = $Member.samaccountname
        $Record."Passord" = $password
        $Record."Klasse" = $GroupName
        $Record."Telefon" = $Member.mobile
        $Record."SkoleEpost" = $Member.mail
        $objRecord = New-Object PSObject -property $Record
        $Table += $objrecord
    }
}

# Eksporter logg
$Table | Select-Object "Klasse", "Fornavn", "Etternavn", "Brukernavn", "Passord", "Telefon", "SkoleEpost" | export-csv -Encoding UTF8 C:\Users\rootandrem\Desktop\PassReset\$groups.csv -NoTypeInformation -Delimiter ';'


# Vent med å lukke vindu
Write-Host "Trykk på en tast for å fortsette ..."
$x = $host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")