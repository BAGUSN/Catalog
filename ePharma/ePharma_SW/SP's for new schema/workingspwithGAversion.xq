tig:drop-stored-procedure("getSearchQueries2"),
tig:create-stored-procedure("getSearchQueries2","
 declare variable $root_node as xs:string {'$root'};
 declare variable $common_str as xs:string {""
 
 declare function local:getStatus($pedID as xs:string?)  {

 for $ps in collection('tig:///ePharma/PedigreeStatus')/PedigreeStatus[PedigreeID = $pedID] 
 for $status in $ps/Status 
 where $status/StatusChangedOn  = $ps/TimeStamp[1]
 return $status/StatusChangedTo/text()
   
};

 ""};
 declare variable $recvd_local_fn_1 {""declare function local:returnPedigrees($root as document-node()? ) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root/*:pedigreeEnvelope/*:pedigree

 ""};

 declare variable $recvd_local_fn_3 {"" 
 return 
 <Record>
 {
  <pedigreeID>{$ped/*:shippedPedigree/*:documentInfo/*:serialNumber/text()}</pedigreeID>,
  <envelopID>{$root/*:pedigreeEnvelope/*:serialNumber/text()}</envelopID>,
  <dateRecieved>{data($ped/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</dateRecieved>,
  <tradingPartner>{$ped/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName/text()}</tradingPartner>,
  <transactionNumber>{$ped/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier/text()}</transactionNumber>,
  <status> 
  {	local:getStatus( $ped/*:shippedPedigree/*:documentInfo/*:serialNumber )
  }</status>,
  <createdBy>System</createdBy>,
  <docURI>{$docURI}</docURI>
 }
 </Record>	
 };
 ""};


 declare variable $shipped_local_fn_1 {""declare function local:returnPedigrees($root as document-node()?) as node()? {
  let $docURI := document-uri( $root )
  for $ped in $root//*:pedigree/*:receivedPedigree
 ""};
 
 declare variable $shipped_local_fn_3 {"" 
 return <Record>
 {
  <pedigreeID>{$ped/*:documentInfo/*:serialNumber/text()}</pedigreeID>,
  <envelopID>N/A</envelopID>,
   <dateRecieved>{data($ped/*:receivingInfo/*:dateReceived)}</dateRecieved>,
  <tradingPartner>{$ped/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName/text()}</tradingPartner>,
  <transactionNumber>{$ped/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier/text()}</transactionNumber>,
  <status> 
  {	local:getStatus( $ped/*:documentInfo/*:serialNumber )
  }</status>,
 <createdBy>System</createdBy>,
 <docURI>{$docURI}</docURI>
 }
 </Record>	
 };
 ""};
 
 declare variable $g_search_elts as node()
		{<Search>
		   <Scenarios>	
			<Scenario>	
				<Collections>
					<Collection>tig:///ePharma/ReceivedPedigree</Collection>	
					<Collection>tig:///ePharma/ShippedPedigree</Collection>	
				</Collections>
		   		<Key>
					<Name>FromDate</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigreeEnvelope/*:date/text()</Path>
						<Operator>&gt;=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>
		   		<Key>
					<Name>ToDate</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigreeEnvelope/*:date/text()</Path>
						<Operator><![CDATA[<=]]></Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	
				<Key>
					<Name>ContainerCode</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigreeEnvelope/*:container/*:containerCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>No</PedPrefix>			
						<Path><![CDATA[<Dummy>{data($ped/*:shippedPedigree/*:documentInfo/*:serialNumber)}</Dummy>]]></Path>
						<Operator>=</Operator>
						<Value>$root/*:pedigreeEnvelope/*:container[*:containerCode = '$UserVal']/*:pedigreeHandle/*:serialNumber</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>NDC</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigreeEnvelope/*:pedigree/*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>LotNumber</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>				
						<Path>*:pedigreeEnvelope/*:container/*:pedigreeHandle/*:lot</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
						
		   		</Key>	
				<Key>
					<Name>TransNo</Name>
					<RootSelection>
					    <Query>	
						<RootPrefix>Yes</RootPrefix>		
						<Path>*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>	
				<Key>
					<Name>PedID</Name>
					<RootSelection>
					    <Query>			
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:shippedPedigree/*:documentInfo/*:serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>TPName</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>*:pedigreeEnvelope/*:sourceRoutingCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	                               
				<Key>
					<Name>Status</Name>
					<RootSelection>
					    <Query>			
						<RootPrefix>No</RootPrefix>
						<Path>local:getStatus( $root/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber/text() )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>No</PedPrefix>	
						<Path>local:getStatus( $ped/*:shippedPedigree/*:documentInfo/*:serialNumber )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				
				<Key>
					<Name>EnvelopeID</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>*:pedigreeEnvelope/*:serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	     			
			</Scenario>
			
			<Scenario>	
				<Collections>
					<Collection>tig:///ePharma/ShippedPedigree</Collection>	
				</Collections>
		   		<Key>
					<Name>FromDate</Name>
					<RootSelection>
					    <Query>		
						<RootPrefix>Yes</RootPrefix>	
						<Path>*:pedigree/*:receivedPedigree/*:receivingInfo/*:dateReceived/text()</Path>
						<Operator>&gt;=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>
		   		<Key>
					<Name>ToDate</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigree/*:receivedPedigree/*:receivingInfo/*:dateReceived/text()</Path>
						<Operator><![CDATA[<=]]></Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
		   		</Key>	
				<Key>
					<Name>ContainerCode</Name>
					<RootSelection>
					</RootSelection>
		   		</Key>	
				<Key>
					<Name>NDC</Name>
					<RootSelection>
					    <Query>		
						<RootPrefix>Yes</RootPrefix>	
						<Path>*:pedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:pedigree/*:shippedPedigree//*:initialPedigree/*:productInfo/*:productCode</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				<Key>
					<Name>LotNumber</Name>
					<RootSelection>
					    <Query>			
						<RootPrefix>Yes</RootPrefix>	
						<Path>*:pedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree//*:initialPedigree/*:itemInfo/*:lot</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>		
						<PedPrefix>Yes</PedPrefix>		
						<Path>*:pedigree/*:shippedPedigree//*:initialPedigree/*:itemInfo/lot</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>	
				<Key>
					<Name>TransNo</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>*:pedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>	
				<Key>
					<Name>PedID</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>
						<Path>*:pedigree/*:receivedPedigree/*:documentInfo/*:serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:documentInfo/*:serialNumber</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>

		   		</Key>	
				<Key>
					<Name>TPName</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>Yes</RootPrefix>			
						<Path>*:pedigree/*:receivedPedigree/*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>Yes</PedPrefix>	
						<Path>*:pedigree/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>	
				<Key>
					<Name>Status</Name>
					<RootSelection>
					    <Query>
						<RootPrefix>No</RootPrefix>			
						<Path>local:getStatus($root/*:pedigree/*:receivedPedigree/*:documentInfo/*:serialNumber/text())</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</RootSelection>
					<PedigreeSelection>
					     <Query>			
						<PedPrefix>No</PedPrefix>	
						<Path>local:getStatus( $ped/*:documentInfo/*:serialNumber )</Path>
						<Operator>=</Operator>
						<Value>$UserVal</Value>
					    </Query>	
					</PedigreeSelection>
		   		</Key>
				

				<Key>
					<Name>EnvelopeID</Name>
					<RootSelection>
					</RootSelection>
		   		</Key>	     		
			</Scenario>
		   </Scenarios>		
		 </Search>	 
		};

    declare function local:getRootWhereClause($root as node()?,$no as xs:integer,$keys as xs:string*,$values as xs:string*) {
	string-join( 
	(	
		
		for $index in 1 to count($keys)
		let $key_val as xs:string := ( $keys[$index] ) cast as xs:string
		let $user_val as xs:string? := ( $values[$index] ) cast as xs:string
		return 
		for $query in $root//Scenario[$no]/Key[Name = $key_val]/RootSelection/Query
		let $val as xs:string := string( $query/Value )
		let $prefx_root as xs:string  :=   string($query/RootPrefix)
		let $root_node_name as xs:string := if(  lower-case($prefx_root)  = 'yes' ) then  '$root/' else ''
		let $val_query as xs:string :=  if( $val = '$UserVal' ) then concat( '''', $user_val ,'''') else $val
		return string-join( ( concat( $root_node_name, string($query/Path)  ), string($query/Operator) , $val_query) , '  ' )
	),' and ' )
  };
      
     declare function local:getUserValReplaced($src as xs:string,$search4 as xs:string,$rep as xs:string) {
  	(: Replaces only the first occurrance :)
  	let $str1 as xs:string? := substring-before( $src , $search4 )
  	let $str2 as xs:string? := substring-after(  $src , $search4 )
  	return if( string-length($str1) = 0 and string-length($str2) = 0 ) then  $src  else string-join( ($str1,$rep,$str2),'' ) 
     };
	
      declare function local:getPedWhereClause($root as node()?,$no as xs:integer,$keys as xs:string*,$values as xs:string*) {
		
         string-join( 
	(
	for $index in 1 to count($keys)
	let $key_val as xs:string := ( $keys[$index] ) cast as xs:string
	let $user_val as xs:string := ( $values[$index] ) cast as xs:string
	return 
	for $query in $root//Scenario[$no]/Key[Name = $key_val]/PedigreeSelection/Query
	let $prefx_ped as xs:string := string( $query/PedPrefix  )
	let $val as xs:string := string( $query/Value )
	let $ped_node_name as xs:string := if( lower-case($prefx_ped) eq 'yes' ) then  '$ped/' else ''
	let $val_query as xs:string :=   if( $val = '$UserVal' ) then concat( '''', $user_val ,'''') else local:getUserValReplaced($val,'$UserVal', $user_val )
	return  string-join( ( concat( $ped_node_name,$query/Path/string() ), $query/Operator/string() , $val_query ) , '  ' )
	
	),' and ' )
	
  };

  declare function local:getAllKeys($root as node()?,$no as xs:integer,$keys as xs:string*){
	for $key in $root//Scenario[$no]/Key[Name = $keys]
	return $key
  };

  declare function local:getQuery( $coluri as xs:string, $root_where_clause as xs:string?,
		$ped_where_clause as xs:string? ,$root_node_name as xs:string,
		$scenario_3 as xs:boolean,
		$common_local_str as xs:string,
		$recvd_start as xs:string,$recvd_end as xs:string,
		$shipped_start as xs:string,$shipped_end as xs:string,
		$count_elements as xs:integer) as xs:string {
  
 let $first_local_fn as xs:string :=  xs:string($common_local_str ) 
 let $second_local_fn_start as xs:string := xs:string ( if( $scenario_3 ) then $shipped_start else $recvd_start )
 let $ped_full_clause as xs:string := xs:string( if( not(empty($ped_where_clause)) and string-length($ped_where_clause) > 0 ) then concat(' where ', $ped_where_clause ) else '' ) 
 let $second_local_fn_end as xs:string := xs:string ( if( $scenario_3 ) then $shipped_end else $recvd_end ) 
 let $for_clause as xs:string := xs:string (  concat( 'for ' , $root_node_name , ' in ' , ' collection(''',  $coluri , ''') ' )   )
 let $root_full_clause as xs:string := xs:string( if( not(empty($root_where_clause)) and string-length($root_where_clause) > 0 ) then concat('where ',$root_where_clause) else if( $count_elements > 0  ) then ' where false() ' else ' ' )
 let $return_clause as xs:string := xs:string( concat( ' return  local:returnPedigrees(' , $root_node_name , ')' ) )
 return concat(  $first_local_fn , ' ' ,
		$second_local_fn_start , ' ',
		$ped_full_clause , ' ' , $second_local_fn_end, ' ' ,
		$for_clause , ' ' , $root_full_clause , ' ',
		$return_clause
	   )

 };

  declare function local:validateElements( $root as node()? ,  $elts as xs:string* )  {

	  for $elt_name in $elts
	  for $sc in $root//Scenario
	  let $key_names  := $sc//Key/Name/text()
	  return if( $elt_name = $key_names ) then fn:true() else error( concat( $elt_name , ' not found in all the scenarios ') )

  };	

  declare variable $search_elt_names_ext as xs:string* external;
  declare variable $search_elt_values_ext as xs:string* external;  
   
  let $search_elt_names := $search_elt_names_ext
  let $search_elt_values := $search_elt_values_ext
  let $validate := local:validateElements( $g_search_elts ,  $search_elt_names )
  let $count_elts as xs:integer := count( $search_elt_names )
  for $index in 1 to count($g_search_elts//Scenario)
  let $root_where_clause as xs:string? := local:getRootWhereClause($g_search_elts,$index,$search_elt_names,$search_elt_values)		
  let $ped_where_clause as xs:string? := local:getPedWhereClause($g_search_elts,$index,$search_elt_names,$search_elt_values)	
  for $col_uri in $g_search_elts//Scenario[$index]/Collections/Collection
  return local:getQuery($col_uri/text(), $root_where_clause ,$ped_where_clause,$root_node, $index = 2 ,$common_str,$recvd_local_fn_1 ,$recvd_local_fn_3 , $shipped_local_fn_1 , $shipped_local_fn_3,$count_elts )
  
")






tig:drop-stored-procedure("ShippingPedigreeDetails"),
tig:create-stored-procedure("ShippingPedigreeDetails","

declare variable $serialNumber as string external;

declare function local:getItemInfo($node as node()){ 
 if( exists($node/*:shippedPedigree/*:itemInfo/*:quantity)) then 
$node/*:shippedPedigree/*:itemInfo/*:quantity
else if( exists($node/*:receivedPedigree/*:receivingInfo/*:itemInfo/*:quantity)) then 
$node/*:receivedPedigree/*:receivingInfo/*:itemInfo/*:quantity

else if (exists($node/*:shippedPedigree/*:repackagedPedigree ))then 
( $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo/*:quantity )
else if( exists($node/*:shippedPedigree/*:initialPedigree/*:itemInfo/*:quantity) )then
( $node/*:shippedPedigree/*:initialPedigree/*:itemInfo/*:quantity) else if(exists($node/*:shippedPedigree/*:pedigree))then  local:getItemInfo($node/*:shippedPedigree/*:pedigree) else local:getItemInfo($node/*:receivedPedigree/*:pedigree)

};
declare function local:getProductInfo($e as node() ){
let $item  := local:getItemInfo($e)
return


if(exists($e/*:shippedPedigree/*:repackagedPedigree)) then(
<drugName>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:drugName)}</drugName>,
<productCode>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:productCode/@type )}</codeType>,

<quantity>{data(local:getItemInfo($e))}</quantity>,
<manufacturer>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:manufacturer)}</manufacturer>,
<dosageForm>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e/*:shippedPedigree/*:repackagedPedigree/*:productInfo/*:containerSize)}</containerSize>
)
else(
<drugName>{data($e//*:initialPedigree/*:productInfo/*:drugName )}</drugName>,
<productCode>{data($e//*:initialPedigree/*:productInfo/*:productCode )}</productCode>,
<codeType>{data($e//*:initialPedigree/*:productInfo/*:productCode/@type )}</codeType>,
<manufacturer>{data($e//*:initialPedigree/*:productInfo/*:manufacturer )}</manufacturer>,

<quantity>{data(local:getItemInfo($e))}</quantity>,
<dosageForm>{data($e//*:initialPedigree/*:productInfo/*:dosageForm )}</dosageForm>,
<strength>{data($e//*:initialPedigree/*:productInfo/*:strength )}</strength>,
<containerSize>{data($e//*:initialPedigree/*:productInfo/*:containerSize)}</containerSize>
)
};


for $e in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree
where $e/*:shippedPedigree/*:documentInfo/*:serialNumber = $serialNumber

return <output>{
<repackage>{exists($e/*:shippedPedigree/*:repackagedPedigree)}</repackage>,
local:getProductInfo($e),
<custName>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</custName>,
<custAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</custAddress>,
<custContact>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</custContact>,
<custPhone>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</custPhone>,
<custEmail>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</custEmail>,
<datesInCustody></datesInCustody>,
<signatureInfoName>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:name)}</signatureInfoName>,
<signatureInfoTitle>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:title)}</signatureInfoTitle>,
<signatureInfoTelephone>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:telephone)}</signatureInfoTelephone>,
<signatureInfoEmail>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:email)}</signatureInfoEmail>,
<signatureInfoUrl>{data($e/*:shippedPedigree/*:signatureInfo/*:signerInfo/*:url)}</signatureInfoUrl>,
<signatureInfoDate>{data($e/*:shippedPedigree/*:signatureInfo/*:signatureDate)}</signatureInfoDate>,
<pedigreeId>{data($e/*:shippedPedigree/*:documentInfo/*:serialNumber)}</pedigreeId>,
<transactionDate>{data($e/*:shippedPedigree/*:transactionInfo/*:transactionDate)}</transactionDate>, 
<transactionType>{data($e/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifierType)}</transactionType>,
<transactionNo>{data($e/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier)}</transactionNo>,
<fromCompany>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName)}</fromCompany>,
<toCompany>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:businessName)}</toCompany>,
<fromShipAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:businessAddress/*:country))}</fromShipAddress>,
<fromBillAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:shippingAddress/*:country))}</fromBillAddress>,
<fromContact>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:name)}</fromContact>,
<fromTitle>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:title)} </fromTitle>,
<fromPhone>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:telephone)}</fromPhone>,
<fromEmail>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:contactInfo/*:email)}</fromEmail>,
<fromLicense>{data($e/*:shippedPedigree/*:transactionInfo/*:senderInfo/*:licenseNumber)}</fromLicense>,
<toShipAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:businessAddress/*:country))}</toShipAddress>,
<toBillAddress>{concat(data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street1),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:street2),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:city),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:stateOrRegion),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:postalCode),',',data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:shippingAddress/*:country))}</toBillAddress>,
<toContact>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:name)}</toContact>,
<toTitle>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:title)}</toTitle>,
<toPhone>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:telephone)}</toPhone>,
<toEmail>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:contactInfo/*:email)}</toEmail>,
<toLicense>{data($e/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber)}</toLicense>


}</output>
")
