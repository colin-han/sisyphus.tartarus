: fillForm initDeploy
  by: @owner;

if ($env = "staging") then
  : fillForm configFeatureToggle
    by: @releaseOwner
    atLatest: 5 days before $releaseDate;
endif

while (proj in $projects)
  : callApi http://api.gitlab.com/v2/pipeline/${proj}
    body: { branch: ${branch} }
    auth: bearer #ciToken;
endwhile

while (holder in $stakeholders)
  : confirm please confirm the release ${version} for env ${env}?
    by: $holder;
endwhile

: callApi http://api.gitlab.com/v2/pipeline/${proj}
  body: { action: deploy }
  auth: bearer #ciToken;