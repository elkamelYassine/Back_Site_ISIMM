import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './professeur.reducer';

export const ProfesseurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const professeurEntity = useAppSelector(state => state.professeur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="professeurDetailsHeading">
          <Translate contentKey="isimmManagerApp.professeur.detail.title">Professeur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{professeurEntity.id}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="isimmManagerApp.professeur.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{professeurEntity.prenom}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="isimmManagerApp.professeur.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{professeurEntity.nom}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="isimmManagerApp.professeur.email">Email</Translate>
            </span>
          </dt>
          <dd>{professeurEntity.email}</dd>
          <dt>
            <span id="matricule">
              <Translate contentKey="isimmManagerApp.professeur.matricule">Matricule</Translate>
            </span>
          </dt>
          <dd>{professeurEntity.matricule}</dd>
          <dt>
            <span id="departement">
              <Translate contentKey="isimmManagerApp.professeur.departement">Departement</Translate>
            </span>
          </dt>
          <dd>{professeurEntity.departement}</dd>
          <dt>
            <span id="titre">
              <Translate contentKey="isimmManagerApp.professeur.titre">Titre</Translate>
            </span>
          </dt>
          <dd>{professeurEntity.titre}</dd>
          <dt>
            <span id="numTel">
              <Translate contentKey="isimmManagerApp.professeur.numTel">Num Tel</Translate>
            </span>
          </dt>
          <dd>{professeurEntity.numTel}</dd>
          <dt>
            <span id="dateDeNaissance">
              <Translate contentKey="isimmManagerApp.professeur.dateDeNaissance">Date De Naissance</Translate>
            </span>
          </dt>
          <dd>
            {professeurEntity.dateDeNaissance ? (
              <TextFormat value={professeurEntity.dateDeNaissance} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="photoDeProfile">
              <Translate contentKey="isimmManagerApp.professeur.photoDeProfile">Photo De Profile</Translate>
            </span>
          </dt>
          <dd>
            {professeurEntity.photoDeProfile ? (
              <div>
                {professeurEntity.photoDeProfileContentType ? (
                  <a onClick={openFile(professeurEntity.photoDeProfileContentType, professeurEntity.photoDeProfile)}>
                    <img
                      src={`data:${professeurEntity.photoDeProfileContentType};base64,${professeurEntity.photoDeProfile}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {professeurEntity.photoDeProfileContentType}, {byteSize(professeurEntity.photoDeProfile)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="isimmManagerApp.professeur.matiere">Matiere</Translate>
          </dt>
          <dd>
            {professeurEntity.matieres
              ? professeurEntity.matieres.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {professeurEntity.matieres && i === professeurEntity.matieres.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="isimmManagerApp.professeur.user">User</Translate>
          </dt>
          <dd>{professeurEntity.user ? professeurEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/professeur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/professeur/${professeurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProfesseurDetail;
