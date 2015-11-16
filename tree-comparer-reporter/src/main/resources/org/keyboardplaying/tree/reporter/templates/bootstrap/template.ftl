<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Comparison report</title>
	<style>
		<#include "bootstrap.min.css">
	</style>
</head>
<body style="padding: 10px;">
	<#assign span = (12 / tree.content.size())?int>
	<div class="container-fluid">
		<div class="row">
			<#list tree.content.iterator() as node>
			<div class="col-xs-${span}"><h4>${node}</h4></div>
			</#list>
		</div>
		<!-- TODO iterate over children -->
	</div>
</body>
</html>
