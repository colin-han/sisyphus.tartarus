# sisyphus.tartarus

一个类似于plantuml的工作流定义语言。

```
: fillForm initDeploy
  by: @owner;

if ($env = staging) then
  : fillForm configFeatureToggle
    by: @releaseOwner
    atLatest: 5 days before $releaseDate;
end

while (proj in $projects)
  : callApi http://api.gitlab.com/v2/pipeline/${proj}
    body: { branch: ${branch} }
    auth: bearer #ciToken;
end

parallel (holder in $stakeholders)
  : confirm please confirm the release ${version} for env ${env}?
    by: $holder;
end

: callApi http://api.gitlab.com/v2/pipeline/${proj}
  body: { action: deploy }
  auth: bearer #ciToken;
```

可以生成一个类似于下图的工作流

![example](http://www.plantuml.com/plantuml/png/dLAzReCm4Dxz53Sm90n1gjI5g6egHPagTUcE31nmhMDRvf8eYdZt7exRf1sxWBcVx-V3cv5ruCFUg7XG9UHvHyRiNDZdEP0btg8txgm0gge-LrK96tUo6DPAKGUB3EqHda2sUxBz4dX0AxD_S1fdE-ftgFaGyDtrlK4PkeCCQ52F-3OZfuxcLyquyjn_X5QVHwYnSm4XIzDRwQyLsfOwfKu36OI53-u3o48s7x3XSPdKDDgO5qyYPc3sPL5eJwkUsEXwrRXzSNmeF7aqPB6OTx_rkJOgh8EspP1UaJGI_l0EphGO8hDayOdNxnlx742CmaVfa0fpNf3iG7OvoWevEu7u14bMIlASdd-5bLsnxqNyq-3DtWLqmy9SGXj_DqnhDRBpQYCaSZs-0000)
