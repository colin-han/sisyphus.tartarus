# sisyphus.tartarus

一个类似于plantuml的工作流定义语言。

```
: fillForm initDeploy
  by: @owner;

if ($env = "staging") then
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

![example](http://www.plantuml.com/plantuml/png/fLF1Yjim5BphAmRNXsoWDXHwyMxJj2nxAWHAxa6sdsqrYYHa9Kke---rP3KrgQ67zKaypvitCthQzevRTpv9Dcprgo8xiE4T4GrRIv9uJxjOJ4VWaJejQxBz7z3JKs5rgLsn-Rxxkjjl1lQN9X0pu5O02gpNIT88AL-qFIN9UWsXX7ic8_ML0Ka270xbzN0encCQ-XuPS2y640rMAQaBFY4P1xL2jSa3N4SAgolr3mnOQbkKK6dLYFQ5k3jRskksbSI0e2P-CqrpneZo_xdxnXtrRa9zHCslFKfgjALRp_E8y6v8rQ9Xm49wm6id965bhFu1eP3w0rKkEbfgkPcgk9HVZ0YUEkTCaUVSYAmLJl8og_Gfltp8ZJ0aXQ8yCCyTd5qtgI-9Mx98Ax7NHr8pJAdhcCDFb9QhgYkGJWVyYjQ20GRSBmmmXd4CPEytcCh1uHpcEPPQRnRzLSLhCo5Kn19SHuX18xsCVK8hFn9-I_ogV6R0FoispNYCwi1RBbISbsLPj1ILBwN_bkp_d_nIxhnoexC2TNWqFlRUQSEsf6h_Ydy3)
